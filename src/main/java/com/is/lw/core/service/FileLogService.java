package com.is.lw.core.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.is.lw.auth.model.User;
import com.is.lw.auth.model.enums.Role;
import com.is.lw.core.model.*;
import com.is.lw.core.repository.*;
import lombok.AllArgsConstructor;
import org.eclipse.persistence.internal.sessions.DirectCollectionChangeRecord;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileLogService {

    private final FileLogRepository repository;
    private final LabWorkService labWorkService;
    private final CoordinatesService coordinatesService;
    private final CoordinatesRepository coordinatesRepository;
    private final LabWorkRepository labWorkRepository;
    private final DisciplineService disciplineService;
    private final DisciplineRepository disciplineRepository;
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final LocationService locationService;
    private final LocationRepository locationRepository;

    public Boolean validateLabWorks(List<LabWork> labWorks) {
        for (LabWork labWork : labWorks) {
            if (labWork.getId() != null && labWork.getId() <= 0) {
                return false;
            }
            if (labWork.getName() == null || labWork.getName().trim().isEmpty()) {
                return false;
            }
            if (labWork.getCoordinates() == null) {
                return false;
            }
            if (labWork.getMinimalPoint() == null || labWork.getMinimalPoint() <= 0) {
                return false;
            }
            if (labWork.getDescription() == null || labWork.getDescription().length() > 1840) {
                return false;
            }
            Coordinates coordinates = labWork.getCoordinates();
            if (coordinates.getY() == null || coordinates.getY() > 540) {
                return false;
            }
            Discipline discipline = labWork.getDiscipline();
            if (discipline != null) {
                if (discipline.getName() == null || discipline.getName().trim().isEmpty()) {
                    return false;
                }
                if (discipline.getLectureHours() == null || discipline.getLectureHours() <= 0) {
                    return false;
                }
                if (discipline.getSelfStudyHours() == null || discipline.getSelfStudyHours() <= 0) {
                    return false;
                }
            }
            Person author = labWork.getAuthor();
            if (author != null) {
                if (author.getName() == null || author.getName().trim().isEmpty()) {
                    return false;
                }
                if (author.getWeight() != null && author.getWeight() <= 0) {
                    return false;
                }
                if (author.getHairColor() == null) {
                    return false;
                }
                if (author.getNationality() == null) {
                    return false;
                }
                Location location = author.getLocation();
                if (location != null) {
                    if (location.getCoordinateX() == null) {
                        return false;
                    }
                    if (location.getCoordinateY() == null) {
                        return false;
                    }
                    if (location.getName() == null || location.getName().trim().isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public List<LabWork> parseXmlFile(String fileContent) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.readValue(fileContent, xmlMapper.getTypeFactory().constructCollectionType(List.class, LabWork.class));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid XML format: " + e.getMessage(), e);
        }
    }

    @Transactional
    public FileLog importLabWorks(String fileContent) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getId();
        FileLog fileLog = FileLog.builder()
                .createdAt(LocalDateTime.now())
                .createdBy(userId)
                .status(false)
                .count(0L)
                .build();
        try {
            List<LabWork> labWorks = parseXmlFile(fileContent);
            if (!validateLabWorks(labWorks)) {
                fileLog.setStatus(false);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return fileLog;
            }
            for (LabWork labWork : labWorks) {
                labWork.setUpdateable(false);
                labWork.getCoordinates().setUpdateable(false);
                labWork.setCreatedBy(user);
                labWork.getCoordinates().setCreatedBy(user);

                boolean existsByName = labWorkRepository.existsByName(labWork.getName());

                if (existsByName) {
                    fileLog.setStatus(false);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return fileLog;
                }

                Optional<Long> disciplineId = Optional.empty();
                if (labWork.getDiscipline() != null) {
                    labWork.getDiscipline().setUpdateable(false);
                    labWork.getDiscipline().setCreatedBy(user);
                    disciplineService.createDiscipline(labWork.getDiscipline());
                    disciplineId = disciplineRepository.findLast(labWork.getDiscipline().getName(), labWork.getDiscipline().getLectureHours(), labWork.getDiscipline().getSelfStudyHours());
                }

                Optional<Long> authorId = Optional.empty();
                Optional<Long> locationId = Optional.empty();
                if (labWork.getAuthor() != null) {
                    if (labWork.getAuthor().getLocation() != null) {
                        labWork.getAuthor().getLocation().setUpdateable(false);
                        labWork.getAuthor().getLocation().setCreatedBy(user);
                        locationService.createLocation(labWork.getAuthor().getLocation());
                        locationId = locationRepository.findLast(labWork.getAuthor().getLocation().getName(), labWork.getAuthor().getLocation().getCoordinateX());
                    }
                    labWork.getAuthor().setUpdateable(false);
                    labWork.getAuthor().setCreatedBy(user);
                    if (locationId.isPresent()) {
                        labWork.getAuthor().getLocation().setId(locationId.get());
                    } else {
                        labWork.getAuthor().setLocation(null);
                    }
                    personService.createPerson(labWork.getAuthor());
                    authorId = personRepository.findLast(labWork.getAuthor().getName(), labWork.getAuthor().getHairColor());
                }

                coordinatesService.createCoordinates(labWork.getCoordinates());
                Optional<Long> coordinatesId = coordinatesRepository.findLast(labWork.getCoordinates().getX(), labWork.getCoordinates().getY());
                if (disciplineId.isPresent()){
                    labWork.getDiscipline().setId(disciplineId.get());
                }
                if (authorId.isPresent()){
                    labWork.getAuthor().setId(authorId.get());
                }
                labWork.getCoordinates().setId(coordinatesId.get());
                labWorkService.createLabWork(labWork);
            }
            fileLog.setStatus(true);
            fileLog.setCount((long) labWorks.size());
            return fileLog;
        } catch (Exception e) {
            fileLog.setStatus(false);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return fileLog;
        }
    }

    @Transactional
    public List<FileLog> listOfFileLogs() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRole().equals(Role.ADMIN)){
            return repository.findAll();
        } else {
            return repository.findAllByCreatedBy(user.getId());
        }
    }
}
