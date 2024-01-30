package fr.uge.revevue.service;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeService {
    private CodeRepository codeRepository;

    public CodeService(){}

    @Autowired
    public CodeService(CodeRepository codeRepository){
        this.codeRepository = codeRepository;
    }

    public Code create(User user,String title, String description, String javaContent, String unitContent){
        var code = new Code(user, title, description, javaContent);
        if (!unitContent.isBlank()){
            code.setUnitContent(unitContent);
        }
        codeRepository.save(code);
        return code;
    }

    public List<Code> findAll(int offset, int limit){
        Pageable page = PageRequest.of(offset, limit);
        return codeRepository.findAll(page);
    }
    
    public List<Code> findByTitleContaining(String keyword, int offset, int limit) {
        Pageable page = PageRequest.of(offset, limit);
        return codeRepository
          .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrUserUsernameContainingIgnoreCase(page, keyword, keyword, keyword);
    }

    public Vote createVote(User user, Code code, Vote.VoteType voteType){
        var vote = new Vote(user, code, voteType);
        code.addVote(vote);
        codeRepository.save(code);
        return vote;
    }
}
