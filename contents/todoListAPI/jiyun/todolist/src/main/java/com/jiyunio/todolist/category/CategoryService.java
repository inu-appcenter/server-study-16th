package com.jiyunio.todolist.category;

import com.jiyunio.todolist.member.Member;
import com.jiyunio.todolist.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    public void createCategory(Long memberId, String categoryName) {
        Member member = memberRepository.findById(memberId).get();
        Category category = Category.builder()
                .member(member)
                .category(categoryName)
                .build();
        categoryRepository.save(category);
    }

    public List<String> getCategory(Long memberId) {
        List<Category> categories = categoryRepository.findByMemberId(memberId);
        List<String> returnCategory = new ArrayList<>();

        for (Category category : categories) {
            returnCategory.add(category.getCategory());
        }
        return returnCategory;
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
