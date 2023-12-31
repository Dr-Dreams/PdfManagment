package com.pdf.pdfmanagment.repository;


import com.pdf.pdfmanagment.entity.PdfFile;
import com.pdf.pdfmanagment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PdfFileRepository extends JpaRepository<PdfFile, Long> {
    List<PdfFile> findByFileNameContainingIgnoreCase(String searchKeyword);

    List<PdfFile> findByUser(User user);

    PdfFile findByUniqueLink(String uniqueLink);

    Optional<PdfFile> findById(Long id);

    List<PdfFile> findByUserAndFileNameContainingIgnoreCase(User user, String searchKeyword);
}
