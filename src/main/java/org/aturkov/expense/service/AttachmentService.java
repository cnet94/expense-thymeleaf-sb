package org.aturkov.expense.service;

import lombok.RequiredArgsConstructor;
import org.aturkov.expense.dao.attachment.AttachmentEntity;
import org.aturkov.expense.dao.attachment.AttachmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;

    public void saveAttachment(UUID detailId, MultipartFile file) throws IOException {
        if (file.isEmpty())
            return;
        String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/uploads";
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = java.nio.file.Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        //todo any users different path
        attachmentRepository.save(new AttachmentEntity()
                .setExpenseDetailId(detailId)
                .setStorageLink(fileNameAndPath.toString()));
    }
}
