/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import wad.domain.FileObject;
import wad.domain.News;

/**
 *
 * @author mmohamud
 */
public interface FileObjectRepository extends JpaRepository<FileObject, Long> {
    @Transactional
    FileObject findByuutiset(News uutiset);
}
