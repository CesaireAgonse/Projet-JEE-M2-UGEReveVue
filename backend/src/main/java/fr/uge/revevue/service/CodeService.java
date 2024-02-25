package fr.uge.revevue.service;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.TestResults;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.entity.Vote;
import fr.uge.revevue.form.UnitTestClassForm;
import fr.uge.revevue.information.CodeInformation;
import fr.uge.revevue.repository.CodeRepository;
import fr.uge.revevue.repository.PostRepository;
import fr.uge.revevue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CodeService {
    private CodeRepository codeRepository;
    private UserRepository userRepository;
    public final static int LIMIT = 3;

    public CodeService(){}

    @Autowired
    public CodeService(CodeRepository codeRepository, UserRepository userRepository){
        this.codeRepository = codeRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public void create(long id, String title, String description, MultipartFile javaContent, MultipartFile unitContent) throws IOException {
        var user = userRepository.findById(id);
        var code = new Code(user.get(), title, description, javaContent.getBytes());
        code.setTestResults(new TestResults());
        if (unitContent != null){
            code.setUnitContent(unitContent.getBytes());
            var results = WebClientService.microServiceExecute(new UnitTestClassForm(javaContent.getBytes(), unitContent.getBytes()));
            code.setTestResults(new TestResults(results.testsTotalCount(), results.testsSucceededCount(), results.testsFailedCount(), results.testsTotalTime()));
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
    public Set<CodeInformation> findWithKeyword(String keyword, int offset, int limit) {
        Pageable page = PageRequest.of(offset, limit);
        var codes = codeRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrUserUsernameContainingIgnoreCase(page, keyword, keyword, keyword);
        return codes.stream().map(CodeInformation::from).collect(Collectors.toSet());
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
