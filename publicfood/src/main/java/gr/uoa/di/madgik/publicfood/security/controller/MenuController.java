package gr.uoa.di.madgik.publicfood.security.controller;

import gr.uoa.di.madgik.publicfood.model.*;
import gr.uoa.di.madgik.publicfood.security.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
public class MenuController {

    @Autowired
    private MenuMetaRepository menuMetaRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private FullMenuEntityRepository fullMenuEntityRepository;
    @Autowired
    private MenuHasRecipeRepository menuHasRecipeRepository;
    @Autowired
    private TranslationRecipeRepository translationRecipeRepository;
    @Autowired
    private ChildrenHasAllergyRepository childrenHasAllergyRepository;
    @Autowired
    private RecipeHasAllergyRepository recipeHasAllergyRepository;
    @Autowired
    private AllergyRepository allergyRepository;
    @Autowired
    private SchoolHasFullMenuRepository schoolHasFullMenuRepository;
    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private TranslationAllergyRepository translationAllergyRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private CookingCenterRepository cookingCenterRepository;
    @Autowired
    private AreaRepository areaRepository;

    @RequestMapping(value = "/menu/getMenuByDate", method = RequestMethod.GET)
    public ResponseEntity<?> getMenuByDate( @RequestParam String domain, @RequestParam String language
            , @RequestParam int year, @RequestParam int month, @RequestParam int day, @RequestParam int schoolId)  {

        List<Integer> weeks = new ArrayList<Integer>();
        LocalDateTime comparableDate =  LocalDateTime.of(year,month,day,0,0);
        SchoolHasFullMenuEntity bySchoolIdAndSchoolDomainName = schoolHasFullMenuRepository.findBySchoolIdAndSchoolDomainName(schoolId, domain);
        FullMenuEntity fullMenuEntity = fullMenuEntityRepository.findByDomainAndId(domain, bySchoolIdAndSchoolDomainName.getFullMenuId());
        List<MenuEntity> byFullMenuId = menuRepository.findByFullMenuId(bySchoolIdAndSchoolDomainName.getFullMenuId());
        Map<Integer, MenuMetaEntity> candidateMenus = new HashMap<Integer, MenuMetaEntity>();
        List<MenuHasRecipeEntity> menuHasRecipeEntities = null;
        List<TranslationRecipeEntity> translationRecipeEntities = null;
        SchoolEntity schoolEntity = schoolRepository.findByIdAndDomainName(schoolId, domain);
        CookingCenterEntity cookingCenter = cookingCenterRepository.findByDomainNameAndId(domain, schoolEntity.getCookingCenterId());
        AreaEntity area = areaRepository.findByDomainNameAndIdarea(domain, cookingCenter.getArea());
        weeks.add(area.getStartingWeek());
        for(int i=1; i<=3; i++){
            if(weeks.get(i-1) <4)
            {
                weeks.add(weeks.get(i-1) + 1);
            }
            else
                weeks.add(1);
        }

        for(MenuEntity m : byFullMenuId){
            MenuMetaEntity byMenuDomainNameAndMenuId = menuMetaRepository.findByMenuDomainNameAndMenuId(domain, m.getId());
           // bySchoolIdAndSchoolDomainName.g
            int startingWeek = 0;
            for(int i = 0; i< weeks.size(); i++)
            {
                if(weeks.get(i) == byMenuDomainNameAndMenuId.getWeek()){
                    startingWeek = i;
                    break;
                }
            }
            int daysFromStart = startingWeek * 7 + byMenuDomainNameAndMenuId.getDay() -1;

            LocalDateTime validFrom = fullMenuEntity.getValidFrom().toLocalDateTime();

            LocalDateTime localDateTime = validFrom.plusDays(daysFromStart);

            long diffInDays = ChronoUnit.DAYS.between(localDateTime, comparableDate);
            if(diffInDays == 0) {
                candidateMenus.put((int) diffInDays, byMenuDomainNameAndMenuId);
                break;
            }
            else if(diffInDays % Integer.parseInt(byMenuDomainNameAndMenuId.getRepeatWeek()) == 0){
                candidateMenus.put((int) diffInDays / 7, byMenuDomainNameAndMenuId);
            }
        }

        MenuMetaEntity finalMenu = null;
        if(candidateMenus.size() >= 1){
            Iterator it = candidateMenus.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                MenuMetaEntity m = (MenuMetaEntity) pair.getValue();
                int diffInDays = (int) pair.getKey();
                if (diffInDays == 0){
                    finalMenu = m;
                    translationRecipeEntities = new ArrayList<TranslationRecipeEntity>();
                    menuHasRecipeEntities = menuHasRecipeRepository.findByMenuIdAndDomainName(m.getMenuId(), domain);
                    for(MenuHasRecipeEntity menuHasRecipeEntity : menuHasRecipeEntities)
                    {
                        TranslationRecipeEntity byRecipeId1AndLanguageAndRecipeDomainName1 = translationRecipeRepository.findByRecipeId1AndLanguageAndRecipeDomainName1(menuHasRecipeEntity.getRecipeId(), language, domain);
                        translationRecipeEntities.add(byRecipeId1AndLanguageAndRecipeDomainName1);
                    }
                    break;
                }
                else if(diffInDays % m.getRepeatInterval() == 0) {
                    finalMenu = m;
                    translationRecipeEntities = new ArrayList<TranslationRecipeEntity>();
                    menuHasRecipeEntities = menuHasRecipeRepository.findByMenuIdAndDomainName(m.getMenuId(), domain);
                    for(MenuHasRecipeEntity menuHasRecipeEntity : menuHasRecipeEntities)
                    {
                        TranslationRecipeEntity byRecipeId1AndLanguageAndRecipeDomainName1 = translationRecipeRepository.findByRecipeId1AndLanguageAndRecipeDomainName1(menuHasRecipeEntity.getRecipeId(), language, domain);
                        translationRecipeEntities.add(byRecipeId1AndLanguageAndRecipeDomainName1);
                    }
                    break;
                }
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
        if(finalMenu != null)
            System.out.println(finalMenu.getRepeatStart());
        else
            System.out.println("No school day!");


        if(!translationRecipeEntities.isEmpty()){
            Collections.sort(translationRecipeEntities, new Comparator<TranslationRecipeEntity>() {
                @Override
                public int compare(TranslationRecipeEntity c1, TranslationRecipeEntity c2) {

                    if(c1 != null && c2 != null)
                        return Integer.compare(c1.getRecipeType(), c2.getRecipeType());
                    else
                        return -1;
                }
            });
        }

        return new ResponseEntity<List<TranslationRecipeEntity>>(translationRecipeEntities, HttpStatus.OK);
    }



    @RequestMapping(value = "/menu/getChildAllergensByDate", method = RequestMethod.GET)
    public ResponseEntity<?> getChildAllergensByDate(HttpServletRequest request, @RequestParam String domain, @RequestParam String language
            , @RequestParam int year, @RequestParam int month, @RequestParam int day, @RequestParam int childId) {


        ChildrenEntity childEntity = childRepository.findByIdchildrenAndDomainName(childId, domain);
        ResponseEntity<List<TranslationRecipeEntity>> menuByDate = (ResponseEntity<List<TranslationRecipeEntity>>) this.getMenuByDate(domain, language, year, month, day, childEntity.getSchoolId());
        List<RecipeHasAllergyEntity> byRecipeIdAndRecipeDomainName = new ArrayList<RecipeHasAllergyEntity>();
        List<ChildrenHasAllergyEntity> childAllergens = childrenHasAllergyRepository.findByChildrenIdchildrenAndDomainName(childId, domain);

        if(menuByDate.getBody() != null && !menuByDate.getBody().isEmpty()) {
            for (TranslationRecipeEntity t : menuByDate.getBody()) {
                if(t != null) {
                    List<RecipeHasAllergyEntity> recipeAllergens = recipeHasAllergyRepository.findByRecipeIdAndRecipeDomainName(t.getRecipeId1(), domain);

                    for (RecipeHasAllergyEntity allergyEntity : recipeAllergens) {
                        byRecipeIdAndRecipeDomainName.add(allergyEntity);
                    }
                }
            }
        }
        Map<Integer,RecipeHasAllergyEntity> uniqueAllergens = new HashMap<Integer, RecipeHasAllergyEntity>();

        for(RecipeHasAllergyEntity recipeHasAllergyEntity : byRecipeIdAndRecipeDomainName)
        {
            uniqueAllergens.put(recipeHasAllergyEntity.getAllergyIdallergy(), recipeHasAllergyEntity);
        }
                //new HashSet<RecipeHasAllergyEntity>(byRecipeIdAndRecipeDomainName);
        List<TranslationAllergyEntity> commonAllergens = new ArrayList<TranslationAllergyEntity>();

        for(ChildrenHasAllergyEntity childAllergen: childAllergens)
        {
            uniqueAllergens.forEach((allergenId,allergen)->{
                if(childAllergen.getAllergyIdallergy() == allergenId && childAllergen.getDomainName().equals(allergen.getRecipeDomainName()))
                {
                    TranslationAllergyEntity allergenEntity = translationAllergyRepository.findByAllergyDomainNameAndLanguageAndAllergyIdallergy(domain, language, childAllergen.getAllergyIdallergy());
                    commonAllergens.add(allergenEntity);
                }
            });
        }

        return new ResponseEntity<List<TranslationAllergyEntity>>(commonAllergens, HttpStatus.OK);

    }

    }
