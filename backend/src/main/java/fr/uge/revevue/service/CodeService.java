package fr.uge.revevue.service;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.UnitTestClassForm;
import fr.uge.revevue.information.CodeInformation;
import fr.uge.revevue.repository.CodeRepository;
import fr.uge.revevue.repository.UserRepository;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CodeService {

    private CodeRepository codeRepository;
    private UserRepository userRepository;
    private EntityManager em;
    private EntityManagerFactory emf;
    private UserRepository userRepository;
    public final static int LIMIT = 3;

    public CodeService(){}

    @Autowired
    public CodeService(CodeRepository codeRepository, UserRepository userRepository, EntityManager em, EntityManagerFactory emf) {
    public CodeService(CodeRepository codeRepository, UserRepository userRepository){
        this.codeRepository = codeRepository;
        this.userRepository = userRepository;
        this.em = em;
        this.emf = emf;
        this.userRepository = userRepository;
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

    public Set<CodeInformation> findAll(int offset, int limit){
        Pageable page = PageRequest.of(offset, limit);
        return codeRepository.findAll(page).stream().map(CodeInformation::from).collect(Collectors.toSet());
    }
    
    public List<CodeInformation> findWithKeyword(String keyword, int offset, int limit) {
        Pageable page = PageRequest.of(offset, limit);
        var codes = codeRepository
                .findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCase(page, keyword, keyword, keyword);
        return codes.stream().map(CodeInformation::from).toList();
    }

    public List<CodeInformation> findWithKeywordByNewest(String keyword, int offset, int limit) {
    @Transactional
    public Set<CodeInformation> findWithKeyword(String keyword, int offset, int limit) {
        Pageable page = PageRequest.of(offset, limit);
        var codes = codeRepository
          .findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCaseOrderByDateDesc(page, keyword, keyword, keyword);
        return codes.stream().map(CodeInformation::from).toList();
    }

    public List<CodeInformation> findWithKeywordByScore(String keyword, int offset, int limit) {
        /*Pageable page = PageRequest.of(offset, limit);

        var codes = codeRepository
          .findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCase(keyword, keyword, keyword);
        var codes2 = codes.stream().map(CodeInformation::from).sorted((o1, o2) -> {
            if(o1.scoreVote() == o2.scoreVote())
                return 0;
            else if(o1.scoreVote() < o2.scoreVote())
                return 1;
            else return -1;
        }).skip(offset*limit).limit(limit).toList();
        return codes2;*/
        Pageable page = PageRequest.of(offset, limit);
        var codes = codeRepository
          .findByTitleContainingOrDescriptionContainingOrUserUsernameContainingAllIgnoreCaseOrderByScoreDesc(page, keyword, keyword, keyword);
        return codes.stream().map(CodeInformation::from).toList();
    }

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
        List<CodeInformation> codes = query.getResultList().stream().map(CodeInformation::from).toList();
        return codes;
    }

    public List<CodeInformation> getCodeFromFollowed(User user, String keyword, int offset, int limit) {
        List<CodeInformation> codes = new ArrayList<>();
        if (user == null) {
            return codes;
        }

        List<User> usersAlreadySeen = new ArrayList<>();
        List<User> followed = userRepository.findFollowedById(user.getId());
        var queueFollow = new ArrayDeque<>(followed);

        var totalCodesAlreadyDisplayed = offset * limit;
        var codesCurrentPageDisplayed = 0;

        while(!queueFollow.isEmpty() && codesCurrentPageDisplayed < limit) {
            var follow = queueFollow.pollFirst();
            queueFollow.addAll(userRepository.findFollowedByIdFilterUsers(follow.getId(), usersAlreadySeen));

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

        /*for (var follow : followed) {
            if (!usersAlreadySeen.contains(follow)) {
                int nbCodesFollow = codeRepository.countByUserIdAndTitleContainingIgnoreCaseOrUserIdAndDescriptionContainingIgnoreCaseOrUserIdAndUserUsernameContainingIgnoreCase(follow.getId(), keyword, follow.getId(), keyword, follow.getId(), keyword);

                if (totalCodesAlreadyDisplayed >= nbCodesFollow) {
                    totalCodesAlreadyDisplayed -= nbCodesFollow;
                } else {
                    int remainingCodesToDisplay = nbCodesFollow - totalCodesAlreadyDisplayed;
                    int codesToAdd = Math.min(remainingCodesToDisplay, limit - codesCurrentPageDisplayed);

                    codes.addAll(getCodeWithLimitAndOffset(follow, keyword, totalCodesAlreadyDisplayed, codesToAdd));
                    codesCurrentPageDisplayed += codesToAdd;

                    totalCodesAlreadyDisplayed = 0;
                }

                usersAlreadySeen.add(follow);

                if (codesCurrentPageDisplayed >= limit) {
                    break;
                }
            }
        }*/

        return codes;
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
}
