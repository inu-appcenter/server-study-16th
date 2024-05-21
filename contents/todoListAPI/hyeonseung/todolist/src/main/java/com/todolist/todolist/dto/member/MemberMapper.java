package com.todolist.todolist.dto.member;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.dto.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


/* Mapper 사용
Service와 Controller 의 목적을 정확히 구분하고,
이들 사이에서 Entity <-> DTO 간의 객체 mapping을 편하게 도와주기 위해
 */
@Mapper
public interface MemberMapper extends EntityMapper<MemberRequestDto, MemberResponseDto,Member> {
    // 해당하는 INSTANCE가 MemberMapper을 상속받아, MemberMapperImpl로 구현하게 될것.
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Override
    @Mapping(target = "todoList",ignore = true) // Member 엔티티가 @Builder 이다 보니. 제거 대상
    Member toEntity(final MemberRequestDto requestDto);
    @Override
    MemberResponseDto toDto(final Member member);

}
