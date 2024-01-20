package fr.uge.revevue.service;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeService {
    private CodeRepository codeRepository;

    public CodeService(){}

    @Autowired
    public CodeService(CodeRepository codeRepository){
        this.codeRepository = codeRepository;
    }

    public Code create(String content, String test){
        var code = new Code(new User(), content);
        if (!test.isBlank()){
            code.setTest(test);
        }
        //codeRepository.save(code);
        return code;
    }



}
