package com.jiyunio.todolist.category;

import com.jiyunio.todolist.customError.CustomException;
import com.jiyunio.todolist.customError.ErrorCode;
import com.jiyunio.todolist.member.Member;
import com.jiyunio.todolist.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    public void createCategory(Long memberId, String categoryName) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                // 회원 존재 안함
                () -> new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_EXIST_MEMBER)
        );
        Category category = Category.builder()
                .member(member)
                .category(categoryName)
                .build();
        categoryRepository.save(category);
    }

    public List<GetCategoryDTO> getCategory(Long memberId) {
        List<Category> categories = categoryRepository.findByMemberId(memberId);
        List<GetCategoryDTO> getCategoryDTO = new ArrayList<>();

        for (Category category : categories) {
            getCategoryDTO.add(GetCategoryDTO.builder()
                    .category(category.getCategory()).build());
        }
        return getCategoryDTO;
    }

    public void updateCategory(Long categoryId, String categoryName) {
        Category category = categoryRepository.findById(categoryId).get();
        category.updateCategory(categoryName);
        categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
