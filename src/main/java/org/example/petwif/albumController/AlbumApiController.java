package org.example.petwif.albumController;

import lombok.RequiredArgsConstructor;
import org.example.petwif.albumDto.AlbumSaveRequestDto;
import org.example.petwif.albumRepository.AlbumRepository;
import org.example.petwif.albumService.AlbumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AlbumApiController {
    private final AlbumService albumService;

    //==앨범 조회==//

    //친구에서 검색
  /*  @GetMapping("/albums")
    public Long findAlbumTitle(){
    }*/


    //==앨범 생성==//
    @PostMapping("/albums/{userId}")
    public Long createAlbum(@RequestParam Long userId, @RequestBody AlbumSaveRequestDto requestDto){
        return albumService.saveAlbum(requestDto);
    }

    //==앨범 수정==//
/*
    @PatchMapping("/albums/{albumId}")
    public Long updateAlbum(@RequestParam Long albumId)
*/


}
