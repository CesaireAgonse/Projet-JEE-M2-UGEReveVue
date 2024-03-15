package fr.uge.revevue.service;

import fr.uge.revevue.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public boolean isExisted(long id){
        return postRepository.existsById(id);
    }
}