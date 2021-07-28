package com.finalproject.ildoduk.service.blog.serviceImpl;

import com.finalproject.ildoduk.dto.blog.BlogFilesDTO;
import com.finalproject.ildoduk.entity.blog.BlogFiles;

import com.finalproject.ildoduk.repository.blog.BlogFileRepository;
import com.finalproject.ildoduk.service.blog.service.BlogFilesService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class BlogFilesServiceImpl implements BlogFilesService {

    private final BlogFileRepository repository;

    @Override
    public void saveFileInDB(BlogFilesDTO dto) {
        BlogFiles entity = dtoToEntity(dto);
        repository.save(entity);
    }

    @Override
    @Transactional
    public void deleteFileInDB(String fileName) {
        repository.deleteByFileName(fileName);
    }

    @Override
    @Transactional
    public void deleteAllFileOnThePost(Long postNo) {

        List<BlogFiles> entities = repository.findAllByBlog_PostNo(postNo);
        log.info(entities.get(0).getFileName());
        String fileRoot = "C:\\summernote_image\\";  // 파일이 저장된 절대 경로

        for(BlogFiles entity : entities){

            BlogFilesDTO dto = entityToDTO(entity);
            String fileName = dto.getFileName();
            log.info(fileName);
            try{

                String fileServerPath = fileRoot + fileName;
                File fileInServer = new File(fileServerPath); // 파일 객체 생성

                if(fileInServer.exists()){ // 파일이 있다면,
                    log.info("file exists");
                    fileInServer.delete(); // 파일 삭제
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        repository.deleteAllByBlog_PostNo(postNo);

    }
}
