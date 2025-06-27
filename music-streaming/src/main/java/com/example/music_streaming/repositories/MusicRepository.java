package com.example.music_streaming.repositories;

import com.example.music_streaming.models.Music;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends ElasticsearchRepository<Music,String> {

    @Query("""
        {
          "match": {
            "searchText": {
              "query": "?0",
              "fuzziness": "AUTO"
            }
          }
        }
    """)
    List<Music> searchByKeyword(String keyword);

    @Query("""
{
  "match": {
    "artistName": {
      "query": "?0",
      "fuzziness": "AUTO"
    }
  }
}
""")
    List<Music> findAllByArtistNamePartial(String keyword);

    @Query("""
{
  "match": {
    "musicName": {
      "query": "?0",
      "fuzziness": "AUTO"
    }
  }
}
""")
    List<Music> findAllByMusicNamePartial(String keyword);

    @Query("""
{
  "match": {
    "album": {
      "query": "?0",
      "fuzziness": "AUTO"
    }
  }
}
""")
    List<Music> findAllByAlbumPartial(String keyword);

}
