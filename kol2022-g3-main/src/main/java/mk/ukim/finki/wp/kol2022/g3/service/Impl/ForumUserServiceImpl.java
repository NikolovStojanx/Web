package mk.ukim.finki.wp.kol2022.g3.service.Impl;

import mk.ukim.finki.wp.kol2022.g3.model.ForumUser;
import mk.ukim.finki.wp.kol2022.g3.model.ForumUserType;
import mk.ukim.finki.wp.kol2022.g3.model.Interest;
import mk.ukim.finki.wp.kol2022.g3.model.exceptions.InvalidForumUserIdException;
import mk.ukim.finki.wp.kol2022.g3.repository.ForumUserRepository;
import mk.ukim.finki.wp.kol2022.g3.service.ForumUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForumUserServiceImpl implements ForumUserService {

    private final ForumUserRepository forumUserRepository;
    private final InterestServiceImpl interestService;

    public ForumUserServiceImpl(ForumUserRepository forumUserRepository, InterestServiceImpl interestService) {
        this.forumUserRepository = forumUserRepository;
        this.interestService = interestService;
    }

    @Override
    public List<ForumUser> listAll() {
        return forumUserRepository.findAll();
    }

    @Override
    public ForumUser findById(Long id) {
        return forumUserRepository.findById(id).orElseThrow(InvalidForumUserIdException::new);
    }

    @Override
    public ForumUser create(String name, String email, String password, ForumUserType type, List<Long> interestId, LocalDate birthday) {
        ForumUser forumUser = new ForumUser(name,
                email,
                password,
                type,
                interestId.stream().map(intId -> interestService.findById(intId)).collect(Collectors.toList()),
                birthday);

        return forumUserRepository.save(forumUser);
    }

    @Override
    public ForumUser update(Long id, String name, String email, String password, ForumUserType type, List<Long> interestId, LocalDate birthday) {
        ForumUser forumUser = this.findById(id);
        forumUser.setName(name);
        forumUser.setEmail(email);
        forumUser.setPassword(password);
        forumUser.setType(type);
        forumUser.setInterests(interestId.stream().map(intId -> interestService.findById(intId)).collect(Collectors.toList()));
        return forumUserRepository.save(forumUser);
    }

    @Override
    public ForumUser delete(Long id) {
        ForumUser forumUser = this.findById(id);
        forumUserRepository.delete(forumUser);
        return forumUser;
    }

    @Override
    public List<ForumUser> filter(Long interestId, Integer age) {
        List<ForumUser> forumUsers;
        if (interestId == null && age == null) {
            forumUsers = forumUserRepository.findAll();

        } else if (interestId != null && age != null) {
            Interest interest = interestService.findById(interestId);
            forumUsers = forumUserRepository.findByInterestsContainingAndBirthdayAfter(interest, LocalDate.now().minusYears(age));

        } else if (interestId != null) {
            Interest interest = interestService.findById(interestId);
            forumUsers = forumUserRepository.findByInterestsContaining(interest);
        } else {
            forumUsers = forumUserRepository.findByBirthdayAfter(LocalDate.now().minusYears(age));
        }
        return forumUsers;
    }
}
