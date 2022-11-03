package com.example.fastcampusmysql.domain.follow.service;

import com.example.fastcampusmysql.domain.follow.dto.FollowDto;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowReadService {

    private final FollowRepository followRepository;

    public List<FollowDto> getFollowings(Long fromMemberId) {
        return followRepository.findAllByFromMemberId(fromMemberId)
                .stream()
                .map(follow -> FollowDto.builder().follow(follow).build())
                .toList();
    }
}
