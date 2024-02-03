package fr.uge.revevue.service;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.Post;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.information.CodeInformation;
import fr.uge.revevue.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CodeService {

    private CodeRepository codeRepository;

    public CodeService(){}

    @Autowired
    public CodeService(CodeRepository codeRepository){
        this.codeRepository = codeRepository;
    }

    public void create(User user, String title, String description, String javaContent, String unitContent){
        var code = new Code(user, title, description, javaContent);
        if (!unitContent.isBlank()){
            code.setUnitContent(unitContent);
        }
        codeRepository.save(code);
    }

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
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrUserUsernameContainingIgnoreCase(page, keyword, keyword, keyword);
        return codes.stream().map(CodeInformation::from).toList();
    }
    
    public List<CodeInformation> findWithKeywordByNewest(String keyword, int offset, int limit) {
        Pageable page = PageRequest.of(offset, limit);
        var codes = codeRepository
          .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrUserUsernameContainingIgnoreCaseOrderByDateDesc(page, keyword, keyword, keyword);
        return codes.stream().map(CodeInformation::from).toList();
    }
    
    public List<CodeInformation> findWithKeywordByScore(String keyword, int offset, int limit) {
        Pageable page = PageRequest.of(offset, limit);
        var codes = codeRepository
          .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrUserUsernameContainingIgnoreCase(keyword, keyword, keyword);
        var codes2 = codes.stream().map(CodeInformation::from).sorted((o1, o2) -> {
            if(o1.scoreVote() == o2.scoreVote())
                return 0;
            else if(o1.scoreVote() < o2.scoreVote())
                return 1;
            else return -1;
        }).skip(offset*limit).limit(limit).toList();
        return codes2;
    }

    @Transactional
    public Code delete (long codeId){
        var code = codeRepository.findById(codeId);
        if(code.isEmpty()){
            throw new IllegalArgumentException("Code not found");
        }
        codeRepository.delete(code.get());
        return code.get();
    }
}
