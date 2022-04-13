package mk.ukim.finki.wp.lab2022.web;

import mk.ukim.finki.wp.lab2022.model.News;
import mk.ukim.finki.wp.lab2022.model.NewsType;
import mk.ukim.finki.wp.lab2022.service.NewsCategoryService;
import mk.ukim.finki.wp.lab2022.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("/")
public class NewsController {

    private final NewsService service;
    private final NewsCategoryService newsCategoryService;

    public NewsController(NewsService service, NewsCategoryService newsCategoryService) {
        this.service = service;
        this.newsCategoryService = newsCategoryService;
    }

    /**
     * This method should use the "list.html" template to display all news.
     * The method should be mapped on paths '/' and '/news'.
     * The arguments that this method takes are optional and can be 'null'.
     * In the case when the arguments are not passed (both are 'null') all news should be displayed.
     * If one, or both of the arguments are not 'null', the news that are the result of the call
     * to the method 'listNewsWithPriceLessThanAndType' from the service should be displayed.
     *
     * @param price
     * @param type
     * @return The view "list.html".
     */
    @GetMapping({"/news","/"})
    public String showNews(@RequestParam(required = false) Double price,
                           @RequestParam(required = false) NewsType type,
                           Model model) {
        List<News> news;
        if (price == null && type == null) {
            news = this.service.listAllNews();
        } else{
            news = this.service.listNewsWithPriceLessThanAndType(price, type);
        }
        model.addAttribute("news",news);
        model.addAttribute("categories",newsCategoryService.listAll());
        List<NewsType> types = new ArrayList<>();
        NewsType a = NewsType.DRAFT;
        NewsType b = NewsType.PROMOTION;
        NewsType c = NewsType.PUBLIC;
        types.add(a);
        types.add(b);
        types.add(c);
        model.addAttribute("types",types);
        return "list";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/news/add'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/news/add")
    public String showAdd(Model model) {
        model.addAttribute("categories",newsCategoryService.listAll());
        List<NewsType> types = new ArrayList<>();
        NewsType a = NewsType.DRAFT;
        NewsType b = NewsType.PROMOTION;
        NewsType c = NewsType.PUBLIC;
        types.add(a);
        types.add(b);
        types.add(c);
        model.addAttribute("types",types);
        return "form";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case all 'input' elements should be filled with the appropriate value for the entity that is updated.
     * The method should be mapped on path '/news/[id]/edit'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/news/{id}/edit")
    public String showEdit(@PathVariable String id,Model model) {
        model.addAttribute("vestToEdit",service.findById(Long.parseLong(id)));
        model.addAttribute("categories",newsCategoryService.listAll());
        List<NewsType> types = new ArrayList<>();
        NewsType a = NewsType.DRAFT;
        NewsType b = NewsType.PROMOTION;
        NewsType c = NewsType.PUBLIC;
        types.add(a);
        types.add(b);
        types.add(c);
        model.addAttribute("types",types);
        return "form";
    }

    /**
     * This method should create a news given the arguments it takes.
     * The method should be mapped on path '/news'.
     * After the entity is created, all news should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/news")
    public String create(@RequestParam String name,
                         @RequestParam String description,
                         @RequestParam Double price,
                         @RequestParam NewsType type,
                         @RequestParam Long category,
                         @RequestParam(required = false) Long vestId) {
//        if(vestId!=null)
//            this.service.update(vestId,name,description,price,type,category);
//        else
            this.service.create(name, description, price, type, category);
        return "redirect:/news";
    }

    /**
     * This method should update a news given the arguments it takes.
     * The method should be mapped on path '/news/[id]'.
     * After the entity is updated, all news should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/news/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam String description,
                         @RequestParam Double price,
                         @RequestParam NewsType type,
                         @RequestParam Long category) {
        this.service.update(id, name, description, price, type, category);
        return "redirect:/news";
    }

    /**
     * This method should delete the news that has the appropriate identifier.
     * The method should be mapped on path '/news/[id]/delete'.
     * After the entity is deleted, all news should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/news/{id}/delete")
    public String delete(@PathVariable String id) {
        this.service.delete(Long.parseLong(id));
        return "redirect:/news";
    }

    /**
     * This method should increase the number of likes of the appropriate news by 1.
     * The method should be mapped on path '/news/[id]/like'.
     * After the operation, all news should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/news/{id}/like")
    public String like(@PathVariable String id) {
        this.service.like(Long.parseLong(id));
        return "redirect:/news";
    }
}
