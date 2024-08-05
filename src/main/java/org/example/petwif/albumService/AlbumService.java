package org.example.petwif.albumService;

import lombok.RequiredArgsConstructor;
import org.example.petwif.albumDto.AlbumSaveRequestDto;
import org.example.petwif.albumRepository.AlbumRepository;
import org.example.petwif.domain.entity.Album;
import org.example.petwif.domain.entity.Member;
import org.example.petwif.memberRepository.MemberRepository;
import org.example.petwif.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository;

    //==앨범 조회==//


    //==앨범 등록==//
   @Transactional
    public String saveAlbum(){//AlbumSaveRequestDto requestDto){
        Member = memberRepository.findByName(requestDto.getMember())
                .orElseThrow(() -> new RuntimeException("member not found"));
        Album album = Album.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .build();

        //return albumRepository.save(album).getId();

       // return albumRepository.save(requestDto.toEntity(member)).getId();
       return "";
    }


    //==앨범 수정==//
   // @Transactional Album updateAlbum()


    //==앨범 삭제==//




