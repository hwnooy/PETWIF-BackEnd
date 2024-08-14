package org.example.petwif.repository;

import org.example.petwif.domain.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {


//    @Query(value = "Select Pet from Pet join Member m where Pet.member.id = :mId")
//    List<Pet> findPetByMemberId(@Param("mId") Long id);

}
