package com.jiyunio.todolist.category;

import com.jiyunio.todolist.ResponseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/category{memberId}")
    public ResponseEntity<ResponseDTO> createCategory(@PathVariable Long memberId, @RequestParam @NotBlank String categoryName) {
        categoryService.createCategory(memberId, categoryName);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("카테고리 생성 성공")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/category/{memberId}")
    public List<GetCategoryDTO> getCategory(@PathVariable Long memberId) {
        return categoryService.getCategory(memberId);
    }

    @PutMapping("/category/{categoryId}")
    public ResponseEntity<ResponseDTO> updateCategory(@PathVariable Long categoryId, @RequestParam @NotBlank String categoryName) {
        categoryService.updateCategory(categoryId, categoryName);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("카테고리 수정 성공")
                .build();
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<ResponseDTO> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        ResponseDTO responseDTO = ResponseDTO.builder()
                .msg("카테고리 삭제 성공")
                .build();
        return new ResponseEntity<>(responseDTO, HttpStatus.NO_CONTENT);
    }
}
