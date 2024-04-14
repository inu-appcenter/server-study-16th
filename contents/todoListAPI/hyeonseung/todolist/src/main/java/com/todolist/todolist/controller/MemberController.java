package com.todolist.todolist.controller;

import com.todolist.todolist.domain.Member;
import com.todolist.todolist.dto.member.MemberResponse;
import com.todolist.todolist.dto.member.MemberRequest;
import com.todolist.todolist.dto.member.UpdateResponse;
import com.todolist.todolist.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest request) {
        Member member = memberService.create(request);
        return ResponseEntity.ok(new MemberResponse(member));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<UpdateResponse> updateMember(@RequestBody MemberRequest request,@PathVariable(value="memberId") Long id){
        Member member = memberService.update(id, request);

        return ResponseEntity.ok(new UpdateResponse(member));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> readOneMember(@PathVariable(value="memberId")Long id){
        Member member = memberService.searchId(id);
        return ResponseEntity.ok(new MemberResponse(member));
    }
}
