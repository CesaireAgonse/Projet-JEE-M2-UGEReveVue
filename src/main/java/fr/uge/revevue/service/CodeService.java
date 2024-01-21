package fr.uge.revevue.service;

import fr.uge.revevue.entity.Code;
import fr.uge.revevue.entity.User;
import fr.uge.revevue.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Code create(User user, String javaContent, String unitContent){
        var code = new Code(user, javaContent);
        if (!unitContent.isBlank()){
            code.setUnitContent(unitContent);
        }
        codeRepository.save(code);
        return code;
    }

    public List<Code> findAll(){
        return codeRepository.findAll();
    }
}
