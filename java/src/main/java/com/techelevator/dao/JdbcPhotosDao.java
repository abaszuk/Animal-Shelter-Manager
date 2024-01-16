package com.techelevator.dao;

import com.techelevator.model.Photos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPhotosDao implements PhotosDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public Photos createPhoto(String photoUrl, int petId) {
        Photos newPhotos = null;

        String sql = "INSERT INTO photos VALUES( ?, ?) returning photo_id";

        try{
            int photoId = jdbcTemplate.queryForObject(sql, int.class, photoUrl, petId);

            newPhotos = new Photos();
            newPhotos.setPhotoId(photoId);
            newPhotos.setPhotoUrl(photoUrl);
            newPhotos.setPetId(petId);

        } catch (Exception ex){
            System.out.println("Something went Awry creating photo: ");
        }
        return newPhotos;
    }

    @Override
    public Photos getPhoto(int photoId) {
        Photos photos = null;

        String sql = "SELECT photo_id FROM photos WHERE photo_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, photoId);

            if(results.next()){
                photos = mapRowToPhotos(results);
            }
        }catch (Exception ex){
            System.out.println("Something went Awry getting Photo: " + ex.getMessage());
        }
        return photos;
    }

    @Override
    public List<Photos> getPhotos(int petId) {
        List<Photos> photos = new ArrayList<>();

        String sql = "SELECT * FROM photos WHERE pet_id = ?;";

        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, petId);

            while(results.next()){
                Photos photo = mapRowToPhotos(results);
                photos.add(photo);
            }

        }catch (Exception ex){
            System.out.println("Something went Awry gettingPhotos: " + ex.getMessage());
        }


        return photos;
    }

    private Photos mapRowToPhotos(SqlRowSet results){
        Photos photos = new Photos();

        photos.setPhotoId(results.getInt("photo_id"));
        photos.setPhotoUrl(results.getString("photo_URL"));
        photos.setPetId(results.getInt("pet_id"));

        return photos;
    }

}
