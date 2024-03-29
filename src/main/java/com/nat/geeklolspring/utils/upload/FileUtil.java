package com.nat.geeklolspring.utils.upload;
import com.nat.geeklolspring.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



@RequiredArgsConstructor
@Service
public class FileUtil {

    private final S3Service s3Service;

    public Map<String, String> uploadFile(MultipartFile file) throws IOException {
        Map<String, String> uploadedPaths = new HashMap<>();

        // 원본 파일을 중복이 없는 랜덤 이름으로 변경
        String newVideoName = "Shorts_" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 이 파일을 날짜별로 관리하기 위해 날짜별 폴더를 생성
        //String newVideoPath = makeDateFormatDirectory(rootFilePath);

        // 파일의 풀 경로를 생성
        //String fullVideoPath = newVideoPath + "/" + newVideoName;

        // 파일 업로드 수행
        //try {
        //    file.transferTo(new File(newVideoPath, newVideoName));
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        // full-path : D:/abc/upload/2024/01/02/dwdqwdqqw-dwdq-frww_고양이.jpg

        String s = s3Service.uploadUoS3Bucket(file.getBytes(), newVideoName);


        //uploadedPaths.put("filePath", fullVideoPath.substring(rootFilePath.length()));
        uploadedPaths.put("filePath", s);


        return uploadedPaths;
    }

    private static String makeDateFormatDirectory(String rootPath) {

        // 오늘 날짜정보 추출
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        String[] dateInfo = {year + "", len2(month), len2(day)};

        String directoryPath = rootPath;
        for (String s : dateInfo) {
            directoryPath += "/" + s;
            File f = new File(directoryPath);
            if(!f.exists()) f.mkdirs();
        }

        return directoryPath;
    }

    private static String len2(int n) {
        return new DecimalFormat("00").format(n);
    }
}