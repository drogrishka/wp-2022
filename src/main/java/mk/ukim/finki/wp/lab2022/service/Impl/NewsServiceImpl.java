package mk.ukim.finki.wp.lab2022.service.Impl;

import mk.ukim.finki.wp.lab2022.model.News;
import mk.ukim.finki.wp.lab2022.model.NewsCategory;
import mk.ukim.finki.wp.lab2022.model.NewsType;
import mk.ukim.finki.wp.lab2022.model.exceptions.InvalidNewsCategoryIdException;
import mk.ukim.finki.wp.lab2022.model.exceptions.InvalidNewsIdException;
import mk.ukim.finki.wp.lab2022.repository.NewsCategoryRepository;
import mk.ukim.finki.wp.lab2022.repository.NewsRepository;
import mk.ukim.finki.wp.lab2022.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final NewsCategoryRepository newsCategoryRepository;

    public NewsServiceImpl(NewsRepository newsRepository, NewsCategoryRepository newsCategoryRepository) {
        this.newsRepository = newsRepository;
        this.newsCategoryRepository = newsCategoryRepository;
    }

    @Override
    public List<News> listAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(InvalidNewsIdException::new);
    }

    @Override
    public News create(String name, String description, Double price, NewsType type, Long category) {
        NewsCategory kategorija = newsCategoryRepository.findById(category).orElseThrow(InvalidNewsCategoryIdException::new);
        News zacuvaj = new News(name, description, price, type, kategorija);
        return newsRepository.save(zacuvaj);
    }

    @Override
    public News update(Long id, String name, String description, Double price, NewsType type, Long category) {
        News news = newsRepository.findById(id).orElseThrow(InvalidNewsIdException::new);
        news.setName(name);
        news.setDescription(description);
        news.setPrice(price);
        news.setType(type);
        NewsCategory kategorija = newsCategoryRepository.findById(category).orElseThrow(InvalidNewsCategoryIdException::new);
        news.setCategory(kategorija);
        return newsRepository.save(news);
    }

    @Override
    public News delete(Long id) {
        News news = newsRepository.findById(id).orElseThrow(InvalidNewsIdException::new);
        newsRepository.delete(news);
        return news;
    }

    @Override
    public News like(Long id) {
        News news = newsRepository.findById(id).orElseThrow(InvalidNewsIdException::new);
        news.setLikes(news.getLikes() + 1);
        newsRepository.save(news);
        return news;
    }

    @Override
    public List<News> listNewsWithPriceLessThanAndType(Double price, NewsType type) {
        if(type == null && price==null)
            return newsRepository.findAll();
        if(type==null)
            return newsRepository.findAllByPriceLessThan(price);
        if(price==null)
            return newsRepository.findAllByType(type);
        return newsRepository.findAllByPriceLessThanAndType(price, type);
    }

}
