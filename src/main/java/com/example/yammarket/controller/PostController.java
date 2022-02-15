package com.example.yammarket.controller;

import com.example.yammarket.dto.ImageFileDto;
import com.example.yammarket.dto.PostDto;
import com.example.yammarket.dto.PostRequestDto;
import com.example.yammarket.model.ImageFiles;
import com.example.yammarket.model.Posts;
import com.example.yammarket.repository.ImageFileRepository;
import com.example.yammarket.service.ImageFileService;
import com.example.yammarket.service.PostService;
import com.example.yammarket.util.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // 하나로 하려면 configuration 처럼
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final ImageFileService fileService;

    // 처음에는 그냥 메인페이지만 보여주는게 맞고
    // 메인페이지("/")로 가서 post의 list를 호출을 해주는게 맞다
    @GetMapping("/views/postList")
    public List<Posts> mainIndex(){
        return postService.getpostList();
    }

    // 일단 반환하는데 Boolean 형인데
    // user정보가 필요하면  @AuthenticationPrincipal UserDetailsImpl userDetails 를 사용하면 된다.
//    @PostMapping("/posts/write0")
//    public Boolean createPost(@RequestBody PostRequestDto requestDto){
//        return postService.createPostInfo(requestDto);
//    }

    @PostMapping("/posts/write1")    // "file"은 프론트의 input name="file" 인듯
    public Boolean createPost(@RequestPart(value = "file")MultipartFile files,
                              @RequestPart(value = "post") PostDto postDto){
    //public Boolean createPost(@RequestParam("file")MultipartFile files, @RequestBody PostDto postDto){
        try {
            System.out.println("~~~ 1");
            String origFilename = files.getOriginalFilename();
                        // 이미지를 파일로 저장하기 위한 name을 만든다.
            String filename = new MD5Generator(origFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String savePath = System.getProperty("user.dir") + "\\files";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + filename;
            files.transferTo(new File(filePath));

            ImageFileDto fileDto = new ImageFileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFileName(filename);
            fileDto.setFilePath(filePath);
            fileDto.setFileSize(files.getSize()); // 내가 추가함

            // 다른 예제들은 fileUrl? 을 dto로 저장하기도 하던데..
            Long fileId = fileService.saveFile(fileDto); // 이미지 파일을 저장한다.
            postDto.setFileId(fileId);  // 저장한 이미지 파일의 아이디를 postDto에 담는다
            postService.savePost(postDto);  // postDto를 저장한다.
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        //return "redirect:/";
        return true;
    }

    @PostMapping("/file/uploadtest")
    public String testupload2(@RequestParam(value = "file")MultipartFile files){
        System.out.println("~~~ : " + files.getSize());
        return "true";
    }

    @PostMapping("/file/upload")
    public String uploadImage(@RequestParam("file") MultipartFile files) {
        //public String uploadImage(@RequestPart MultipartFile files){
        String filePath = "";
        try {
            System.out.println("~~~ 2");
            String origFilename = files.getOriginalFilename();
            // 이미지를 파일로 저장하기 위한 name을 만든다
            String filename = new MD5Generator(origFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String savePath = System.getProperty("user.dir") + "\\files";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            filePath = savePath + "\\" + filename;

            // 파일 경로만 가져오는 것 이기 때문에 이거 안해도 될 것 같은데..
            //files.transferTo(new File(filePath));

            // 파일 저장할때 게시글을 저장한게 아니기 때문에 이건 저장 안해도 될듯하다
            // ㅇㅇ 어차피 fileDto는 밑에서 saveFile(fileDto) 해주려고 만드는 애다
//            ImageFileDto fileDto = new ImageFileDto();
//            fileDto.setOrigFilename(origFilename);
//            fileDto.setFileName(filename);
//            fileDto.setFilePath(filePath);
//            fileDto.setFileSize(files.getSize()); // 내가 추가함

            // 다른 예제들은 fileUrl? 을 dto로 저장하기도 하던데..
//            Long fileId = fileService.saveFile(fileDto);
//            postDto.setFileId(fileId);
//            postService.savePost(postDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    // 근데 이게 어차피
    /*@PostMapping("/posts/write2")   // 2번째는 PostDto에 file의 path 가 들어있다.
    public Boolean createPost2(@RequestBody PostDto postDto){
        try {
            System.out.println("~~~ w2");
            String origFilename = files.getOriginalFilename();
            // 이미지를 파일로 저장하기 위한 name을 만든다.
            String filename = new MD5Generator(origFilename).toString();
            *//* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. *//*
            String savePath = System.getProperty("user.dir") + "\\files";
            *//* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. *//*
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + filename;
            files.transferTo(new File(filePath));

            ImageFileDto fileDto = new ImageFileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFileName(filename);
            fileDto.setFilePath(filePath);
            fileDto.setFileSize(files.getSize()); // 내가 추가함

            // 다른 예제들은 fileUrl? 을 dto로 저장하기도 하던데..
            Long fileId = fileService.saveFile(fileDto); // 이미지 파일을 저장한다.
            postDto.setFileId(fileId);  // 저장한 이미지 파일의 아이디를 postDto에 담는다
            postService.savePost(postDto);  // postDto를 저장한다.
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        //return "redirect:/";
        return true;
    }*/


    //  @AuthenticationPrincipal UserDetailsImpl userDetails 넣을까 말까
    // 댓글은 댓글조회 url을 만들어서 불러주는게 맞는듯
    @GetMapping("/posts/{postId}")
    public Posts viewPost(@PathVariable Long postId){
        return postService.viewPostInfo(postId);
    }

    // Long 으로 반환해서 뭐하려고
    @PatchMapping("/posts/{postId}")
    public Boolean updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto){
        return postService.updatePost(postId, requestDto);
    }

    @DeleteMapping("/posts/{postId}")
    public Boolean deletePost(@PathVariable Long postId){
        return postService.deletePostService(postId);
    }

}
