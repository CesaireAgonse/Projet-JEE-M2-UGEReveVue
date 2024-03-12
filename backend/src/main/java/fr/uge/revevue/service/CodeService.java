package fr.uge.revevue.service;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.TestResults;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.form.UnitTestClassForm;
import fr.uge.revevue.information.code.CodeInformation;
import fr.uge.revevue.information.code.FilterInformation;
import fr.uge.revevue.information.user.UserInformation;
import fr.uge.revevue.repository.CodeRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.*;

@Service
public class CodeService {

    private CodeRepository codeRepository;
    private UserRepository userRepository;
    private UserService userService;
    private EntityManager em;
    private EntityManagerFactory emf;
    public final static int LIMIT = 10;

    public CodeService(){}

    @Autowired
    public CodeService(CodeRepository codeRepository, UserRepository userRepository, EntityManager em, EntityManagerFactory emf, UserService userService) {
        this.codeRepository = codeRepository;
        this.em = em;
        this.emf = emf;
        this.userRepository = userRepository;
        this.userService = userService;
    }
    @Transactional
    public void create(long id, String title, String description, MultipartFile javaContent, MultipartFile unitContent) throws IOException {
        var user = userRepository.findById(id);
        var code = new Code(user.get(), title, description, javaContent.getBytes());
        if (unitContent != null && !unitContent.isEmpty()){
            code.setUnitContent(unitContent.getBytes());
            var results = WebClientService.microServiceExecute(new UnitTestClassForm(javaContent.getBytes(), unitContent.getBytes()));
            if (results == null){
                throw new IllegalStateException("results is null");
            }
            code.setTestResults(new TestResults(results.testsTotalCount(), results.testsSucceededCount(), results.testsFailedCount(), results.testsTotalTime(), results.failures()));
        }
        codeRepository.save(code);
    }

    @Transactional
    public CodeInformation getInformation(long idCode){
        var code = codeRepository.findById(idCode);
        if(code.isEmpty()){
            throw new IllegalArgumentException("Code not found");
        }
        return CodeInformation.from(code.get());
    }

    @Transactional
    public List<CodeInformation> findWithKeyword(String keyword, int offset, int limit) {
        Pageable page = PageRequest.of(offset, limit);
        var codes = codeRepository
                .findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCase(page, keyword, keyword, keyword);
        return codes.stream().map(CodeInformation::from).toList();
    }
    @Transactional
    public List<CodeInformation> findWithKeywordByNewest(String keyword, int offset, int limit) {
        Pageable page = PageRequest.of(offset, limit);
        var codes = codeRepository
          .findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCaseOrderByDateDesc(page, keyword, keyword, keyword);
        return codes.stream().map(CodeInformation::from).toList();
    }

    @Transactional
    public List<CodeInformation> findWithKeywordByScore(String keyword, int offset, int limit) {
        Pageable page = PageRequest.of(offset, limit);
        var codes = codeRepository
          .findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCaseOrderByScoreDesc(page, keyword, keyword, keyword);
        return codes.stream().map(CodeInformation::from).toList();
    }

    @Transactional
    public List<CodeInformation> getCodeWithLimitAndOffset(User user, String keyword, int offset, int limit) {
        var q = "SELECT c FROM Code c WHERE c.user.username = :username" +
          " AND (LOWER(c.description) LIKE :keyword" +
          " OR LOWER(c.title) LIKE :keyword" +
          " OR LOWER(c.user.username) LIKE :keyword)";
        var query = em.createQuery(q, Code.class);
        query.setParameter("keyword", "%"+keyword.toLowerCase()+"%");
        query.setParameter("username", user.getUsername());
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList().stream().map(CodeInformation::from).toList();
    }
    
    ArrayDeque<User> addNonFollowToQueueFollow(ArrayDeque<User> queueFollow, List<User> usersAlreadySeen) {
        System.out.println("Pas de follow");
        var nonFollowers = userRepository.findUserFilterUsers(usersAlreadySeen);
        queueFollow.addAll(nonFollowers);
        return queueFollow;
    }

    @Transactional
    public List<CodeInformation> getCodeFromFollowed(User user, String keyword, int offset, int limit) {
        List<CodeInformation> codes = new ArrayList<>();
        List<User> usersAlreadySeen = new ArrayList<>();
        List<User> followed = userRepository.findFollowedById(user.getId());
        var queueFollow = new ArrayDeque<>(followed);
        var totalCodesAlreadyDisplayed = offset * limit;
        var codesCurrentPageDisplayed = 0;
        var foundAllFollows = false;
        if(queueFollow.isEmpty()) {
            queueFollow = addNonFollowToQueueFollow(queueFollow, usersAlreadySeen);
            foundAllFollows = true;
        }
        while(!queueFollow.isEmpty() && codesCurrentPageDisplayed < limit) {
            var follow = queueFollow.pollFirst();
            queueFollow.addAll(userRepository.findFollowedByIdFilterUsers(follow.getId(), usersAlreadySeen));
            if(queueFollow.isEmpty() && !foundAllFollows) {
                queueFollow = addNonFollowToQueueFollow(queueFollow, usersAlreadySeen);
                foundAllFollows = true;
            }
            if (!usersAlreadySeen.contains(follow)) {
                var nbCodesFollow = codeRepository.countByUserIdAndTitleContainingIgnoreCaseOrUserIdAndDescriptionContainingIgnoreCaseOrUserIdAndUserUsernameContainingIgnoreCase(follow.getId(), keyword, follow.getId(), keyword, follow.getId(), keyword);
                if (totalCodesAlreadyDisplayed >= nbCodesFollow) {
                    totalCodesAlreadyDisplayed -= nbCodesFollow;
                }
                else {
                    var remainingCodesToDisplay = nbCodesFollow - totalCodesAlreadyDisplayed;
                    var codesToAdd = Math.min(remainingCodesToDisplay, limit - codesCurrentPageDisplayed);
                    codes.addAll(getCodeWithLimitAndOffset(follow, keyword, totalCodesAlreadyDisplayed, codesToAdd));
                    codesCurrentPageDisplayed += codesToAdd;
                    totalCodesAlreadyDisplayed = 0;
                }
                usersAlreadySeen.add(follow);
            }
        }
        return codes;
    }

    @Transactional
    public FilterInformation filter(String sortBy, String query, Integer pageNumber){
        var user = userService.currentUser();
        if(pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }
        List<CodeInformation> codes;
        var count = codeRepository.countByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrUserUsernameContainingIgnoreCase(query, query, query);
        int maxPageNumber = (count - 1) / CodeService.LIMIT;
        System.out.println("count = " + count);
        System.out.println("CodeService.LIMIT = " + CodeService.LIMIT);
        System.out.println("MAX PAGE = " + maxPageNumber);
        switch (sortBy != null ? sortBy : "") {
            // Display all codes by newest
            case "newest" -> {
                codes = findWithKeywordByNewest(query, pageNumber, CodeService.LIMIT);
            }
            // Display all codes by relevance
            case "relevance"-> {
                codes = findWithKeywordByScore(query, pageNumber, CodeService.LIMIT);
            }
            default -> {
                if(user != null) {
                    // Display codes from follows
                    codes = getCodeFromFollowed(user, query, pageNumber, CodeService.LIMIT);
                }
                else {
                    // Display all codes
                    codes = findWithKeyword(query, pageNumber, CodeService.LIMIT);
                }
            }
        }
        return new FilterInformation(codes, sortBy, query, pageNumber, maxPageNumber);
    }

    @Transactional
    public CodeInformation delete (long codeId){
        var code = codeRepository.findById(codeId);
        if(code.isEmpty()){
            throw new IllegalArgumentException("Code not found");
        }
        codeRepository.delete(code.get());
        return CodeInformation.from(code.get());
    }

    @Transactional
    public List<CodeInformation> getAllCodesFromUserId(long userId){
        List<CodeInformation> codes = new ArrayList<>();
        var codesFromUser = codeRepository.findAllByUserId(userId);
        for (var code : codesFromUser){
            codes.add(CodeInformation.from(code));
        }
        return codes;
    }

    @Transactional
    public long countCodesFromUser(UserInformation user){
        var realUser = userRepository.findByUsername(user.username());
        return codeRepository.countByUserId(realUser.get().getId());
    }

}
