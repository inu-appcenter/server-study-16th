package com.todolist.todolist.dto.member;

import com.todolist.todolist.domain.Member;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-27T17:08:26+0900",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.10 (Oracle Corporation)"
)
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member toEntity(MemberRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.name( requestDto.getName() );
        member.loginId( requestDto.getLoginId() );
        member.password( requestDto.getPassword() );

        return member.build();
    }

    @Override
    public MemberResponseDto toDto(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberResponseDto.MemberResponseDtoBuilder memberResponseDto = MemberResponseDto.builder();

        memberResponseDto.id( member.getId() );
        memberResponseDto.name( member.getName() );
        memberResponseDto.loginId( member.getLoginId() );
        memberResponseDto.createdAt( member.getCreatedAt() );
        memberResponseDto.modifiedAt( member.getModifiedAt() );

        return memberResponseDto.build();
    }
}
