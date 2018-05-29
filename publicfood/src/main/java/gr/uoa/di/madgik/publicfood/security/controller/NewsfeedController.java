package gr.uoa.di.madgik.publicfood.security.controller;

import gr.uoa.di.madgik.publicfood.Utilities.StatisticsResults;
import gr.uoa.di.madgik.publicfood.model.AnnouncementEntity;
import gr.uoa.di.madgik.publicfood.model.AnnouncementHasAreaEntity;
import gr.uoa.di.madgik.publicfood.model.TranslationRecipeEntity;
import gr.uoa.di.madgik.publicfood.security.repository.AnnouncementHasAreaRepository;
import gr.uoa.di.madgik.publicfood.security.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@RestController
public class NewsfeedController {

    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    AnnouncementHasAreaRepository announcementHasAreaRepository;

    @RequestMapping(value = "/newsfeed/getAllAnnouncements", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAnnouncements(@RequestParam String domain) {

        List<AnnouncementEntity> announcements = new ArrayList<AnnouncementEntity>();
        List<AnnouncementEntity> allAnnouncements = announcementRepository.findByDomain(domain);
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        for (AnnouncementEntity announcementEntity : allAnnouncements) {
            if (currentTimestamp.after(announcementEntity.getValidFrom()) && currentTimestamp.before(announcementEntity.getValidTo()))
                announcements.add(announcementEntity);
        }

        Collections.sort(announcements, new Comparator<AnnouncementEntity>() {
            @Override
            public int compare(AnnouncementEntity c1, AnnouncementEntity c2) {
                return c1.getCreatedAt().compareTo(c2.getCreatedAt());
            }
        });
        return new ResponseEntity<List<AnnouncementEntity>>(announcements, HttpStatus.OK);
    }

    @RequestMapping(value = "/newsfeed/getAnnouncements", method = RequestMethod.GET)
    public ResponseEntity<?> getAnnouncements(@RequestParam String domain, @RequestParam int area, @RequestParam boolean filter) {

        List<AnnouncementEntity> announcements = new ArrayList<AnnouncementEntity>();
        List<AnnouncementHasAreaEntity> announcementHasAreaEntities = announcementHasAreaRepository.findByDomainAndAreaId(domain, area);
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        for (AnnouncementHasAreaEntity announcementHasAreaEntity : announcementHasAreaEntities) {
            AnnouncementEntity announcement = announcementRepository.findByDomainAndId(domain, announcementHasAreaEntity.getAnnouncementId());
            if(announcement != null) {
                if (currentTimestamp.after(announcement.getValidFrom()) && currentTimestamp.before(announcement.getValidTo()))
                    announcements.add(announcement);
            }
        }

        Collections.sort(announcements, new Comparator<AnnouncementEntity>() {
            @Override
            public int compare(AnnouncementEntity c1, AnnouncementEntity c2) {
                return c1.getCreatedAt().compareTo(c2.getCreatedAt());
            }
        });

        if (filter) {
            if (announcements.size() > 4) {
                List<AnnouncementEntity> lastFourAnnouncements = new ArrayList<AnnouncementEntity>();
                for (int i = announcements.size() - 1; i >= announcements.size() - 4; i--) {
                    lastFourAnnouncements.add(announcements.get(i));
                }
                return new ResponseEntity<List<AnnouncementEntity>>(lastFourAnnouncements, HttpStatus.OK);
            }
        }

        return new ResponseEntity<List<AnnouncementEntity>>(announcements, HttpStatus.OK);


    }

    @RequestMapping(value = "/newsfeed/publishAnnouncement", method = RequestMethod.POST)
    public ResponseEntity<?> publishAnnouncement(@RequestParam String domain, @RequestParam List<Integer> area, @RequestParam int type,
                                                 @RequestParam String link, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date valid_from, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date valid_to) {

        Integer lastID, lastAnnouncementID;
        lastAnnouncementID = announcementRepository.getLastID();
        if (lastAnnouncementID == null)
            lastAnnouncementID = 1;
        else
            lastAnnouncementID++;
        AnnouncementEntity announcement = new AnnouncementEntity();

        announcement.setId(lastAnnouncementID);
        announcement.setDomain(domain);
        announcement.setType(type);
        announcement.setLink(link);
        announcement.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        announcement.setValidFrom(new java.sql.Timestamp(valid_from.getTime()));
        announcement.setValidTo(new java.sql.Timestamp(valid_to.getTime()));

        AnnouncementEntity save = announcementRepository.save(announcement);
        lastID = announcementHasAreaRepository.getLastID();
        if (lastID == null)
            lastID = 1;
        else
            lastID++;
        for (Integer i : area) {
            AnnouncementHasAreaEntity announcementHasAreaEntity = new AnnouncementHasAreaEntity();
            announcementHasAreaEntity.setDomain(domain);
            announcementHasAreaEntity.setAreaId(i);
            announcementHasAreaEntity.setAnnouncementId(save.getId());
            announcementHasAreaEntity.setId(lastID);
            announcementHasAreaRepository.save(announcementHasAreaEntity);
            lastID++;
        }

        return new ResponseEntity<AnnouncementEntity>(save, HttpStatus.OK);
    }


    @RequestMapping(value = "/newsfeed/announcement/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deleteAnnouncement(@PathVariable int id, @RequestParam String domain) {

        AnnouncementEntity announcement = announcementRepository.findByDomainAndId(domain, id);
        if (announcement != null) {
            List<AnnouncementHasAreaEntity> announcementsHaveArea = announcementHasAreaRepository.findByDomainAndAnnouncementId(domain, announcement.getId());

            for(AnnouncementHasAreaEntity announcementHasAreaEntity : announcementsHaveArea ){
                announcementHasAreaRepository.delete(announcementHasAreaEntity);
            }
            announcementRepository.delete(announcement);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }
}
