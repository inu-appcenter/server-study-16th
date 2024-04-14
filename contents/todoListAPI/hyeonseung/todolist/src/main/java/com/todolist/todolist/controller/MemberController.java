package com.todolist.todolist.controller;

import com.todolist.todolist.dto.member.MemberResponseDto;
import com.todolist.todolist.dto.member.MemberRequestDto;
import com.todolist.todolist.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(@RequestBody MemberRequestDto request) {
        MemberResponseDto responseDto = memberService.create(request);
//        return ResponseEntity.status(201).body(responseDto);
        return new ResponseEntity<MemberResponseDto>(responseDto,HttpStatus.CREATED);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(@RequestBody MemberRequestDto request, @PathVariable(value="memberId") Long id){
        MemberResponseDto responseDto = memberService.update(id, request);

        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> readOneMember(@PathVariable(value="memberId")Long id){
        MemberResponseDto responseDto = memberService.searchId(id);
      //  return ResponseEntity.status(200).body(responseDto);
        return new ResponseEntity<MemberResponseDto>(responseDto,HttpStatus.OK);
    }
}
