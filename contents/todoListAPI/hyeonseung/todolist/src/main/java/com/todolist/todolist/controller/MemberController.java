package com.todolist.todolist.controller;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.dto.member.MemberResponse;
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
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequestDto request) {
        MemberResponse responseDto = memberService.create(request);
//        return ResponseEntity.status(201).body(responseDto);
        return new ResponseEntity<MemberResponse>(responseDto,HttpStatus.CREATED);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMember(@RequestBody MemberRequestDto request, @PathVariable(value="memberId") Long id){
        MemberResponse responseDto = memberService.update(id, request);

        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> readOneMember(@PathVariable(value="memberId")Long id){
        MemberResponse responseDto = memberService.searchId(id);
      //  return ResponseEntity.status(200).body(responseDto);
        return new ResponseEntity<MemberResponse>(responseDto,HttpStatus.OK);
    }
}
