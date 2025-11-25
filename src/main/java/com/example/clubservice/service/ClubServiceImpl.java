package com.example.clubservice.service;

import com.example.clubservice.dto.request.ClubCreateRequest;
import com.example.clubservice.dto.request.ClubUpdateRequest;
import com.example.clubservice.dto.response.ClubResponse;
import com.example.clubservice.entity.Club;
import com.example.clubservice.enums.ClubCategory;
import com.example.clubservice.repository.ClubRepository;
import com.example.clubservice.exception.CustomException;
import com.example.clubservice.exception.error.ErrorCode;
import com.example.clubservice.dto.response.ClubListResponse;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClubServiceImpl implements ClubService{

    private final ClubRepository clubRepository;

    @Override
    public void createClub(ClubCreateRequest request) {
        if (clubRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("해당 이름의 동아리가 이미 존재합니다.");
        }

        Club club = Club.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .createdAt(LocalDateTime.now())
                .build();

        clubRepository.save(club);
    }

    @Override
    public List<ClubListResponse> getAllClubs() {
        return clubRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ClubListResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClubListResponse> findAllByCategory(ClubCategory category) {
        if (category == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        List<Club> clubs = clubRepository.findAllByCategory(category);

        if (clubs.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        return clubs.stream()
                .map(ClubListResponse::from)
                .toList();
    }

    @Override
    public void updateClub(Long id, ClubUpdateRequest updateRequest) {
        if (id == null || id <= 0 || updateRequest == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        club.update(updateRequest);

        clubRepository.save(club);
    }

    @Override
    public void deleteClub(Long id) {
        if (id == null || id <= 0) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        if (!clubRepository.existsById(id)) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        clubRepository.deleteById(id);
    }

    public void writeClubExcel(OutputStream os) throws IOException {
        Workbook workbook = new XSSFWorkbook();                         // 새 엑셀 워크북 생성
        Sheet sheet = workbook.createSheet("Clubs");                     // 시트 생성

        // 헤더 작성
        Row header = sheet.createRow(0);                                 // 헤더 생성
        header.createCell(0).setCellValue("동아리명");
        header.createCell(1).setCellValue("설명");
        header.createCell(2).setCellValue("카테고리");

        // DTO 리스트 가져오기
        List<ClubResponse> clubs = clubRepository.findAll()              // 엔티티 조회
                .stream()
                .map(ClubResponse::from)                                 // DTO로 변환
                .toList();

        int rowIdx = 1;
        for (ClubResponse club : clubs) {
            Row row = sheet.createRow(rowIdx++);                         // 새로운 행 생성
            row.createCell(0).setCellValue(club.getName());
            row.createCell(1).setCellValue(club.getDescription());
            row.createCell(2).setCellValue(club.getCategory().name());
        }

        workbook.write(os);                                              // 엑셀 파일로 출력
        workbook.close();                                                // 리소스 정리
    }
}
