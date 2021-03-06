package org.ssa.ironyard.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.ssa.ironyard.controller.mapper.EpisodeMapper;
import org.ssa.ironyard.controller.mapper.PlaylistMapper;
import org.ssa.ironyard.controller.mapper.ShowMapper;
import org.ssa.ironyard.crypto.BCryptSecurePassword;
import org.ssa.ironyard.model.Address;
import org.ssa.ironyard.model.Episode;
import org.ssa.ironyard.model.Playlist;
import org.ssa.ironyard.model.ResponseObject;
import org.ssa.ironyard.model.Show;
import org.ssa.ironyard.model.User;
import org.ssa.ironyard.model.Address.State;
import org.ssa.ironyard.model.Address.ZipCode;
import org.ssa.ironyard.model.ResponseObject.STATUS;
import org.ssa.ironyard.service.PlaylistService;

import com.mysql.cj.jdbc.MysqlDataSource;


public class PLCTest {

PlaylistController controller;

    PlaylistController playlistController;
    PlaylistMapper playlistMapper;
    EpisodeMapper episode;
    
    Playlist playlist;
    Playlist playlist2;
    
    List<Playlist> playlists = new ArrayList<>();
    
    ShowMapper show;
    User testUser;
    
    PlaylistService service;

    @Before
    public void setUp() throws Exception {

        playlistMapper = new PlaylistMapper();
        episode = new EpisodeMapper();
        show = new ShowMapper();

        
        
        testUser = User.builder().id(1).email("test@test.com").firstName("Bob").lastName("Loblaw")
            .address(Address.builder().street("123 Mockingbird Ln").city("Mockingbird Heights").state(State.ALABAMA)
                .zip(new ZipCode("12345")).build())
            .password(new BCryptSecurePassword().secureHash("munsters")).build();
        
        show.setId(2362);
        show.setName("Science Magazine Podcast");
       
        episode.setDescription(
            "News stories on our earliest hunting companions, should we seed exoplanets with life, and finding space storm hot spots with David Grimm.  \n\nFrom the magazine\n\nTwo years ago, 43 students disappeared from a teacher’s college in Guerrero, Mexico. Months of protests and investigation have not yielded a believable account of what happened to them. The government of Mexico claims that the students were killed by cartel members and burned on an outdoor pyre in a dump outside Cucola. Lizzie Wade has been following this story with a focus on the science of fire investigation. She talks about an investigator in Australia that has burned pig carcasses in an effort to understand these events in Mexico.  \n\n[Image: Edgard Garrido/Reuters; Music: Jeffrey Cook]\n");
        episode.setDuration(1542);
        episode.setEpisodeId(199025);
        episode.setFileUrl("https://www.audiosear.ch/media/audio_file/304ba/SciencePodcast_160916.mp3");
        episode.setGenreId(6);
        episode.setName(
            "Podcast: A burning body experiment, prehistoric hunting dogs, and seeding life on other planets");
        episode.setShow(show);
        
        List<EpisodeMapper> episodes = new ArrayList<>();
        episodes.add(episode);
        
        playlistMapper.setId(1);
        playlistMapper.setCurrentDuration(0);
        playlistMapper.setTargetDuration(7200);
        playlistMapper.setName("Test Playlist - Andy");
        playlistMapper.setEpisodes(episodes);
     
        
        playlist = Playlist.builder()
                .id(playlistMapper.getId())
                .name(playlistMapper.getName())
                .user(User.builder().id(playlistMapper.getId()).build())
                .targetDuration(playlistMapper.getTargetDuration())
                .currentDuration(playlistMapper.getCurrentDuration())
                .episodes(playlistMapper.getEpisodes()
                    .stream()
                    .map(e -> {
                                return Episode.builder()
                                .id(e.getId())
                                .duration(e.getDuration())
                                .genreId(e.getGenreId())
                                .description(e.getDescription())
                                .episodeId(e.getEpisodeId())
                                .fileUrl(e.getFileUrl())
                                .name(e.getName())
                                .show(new Show(e.getShow().getId(), e.getShow().getName(), e.getShow().getThumbUrl()))
                                .build();
                        }
                    ).collect(Collectors.toList()))
                .build();        
        
        playlist2 = Playlist.builder(playlist).id(2).build();
        
        playlists.add(playlist);
        playlists.add(playlist2);
       

    }
    
    @Before
    public void mock()
    {


        service = EasyMock.createNiceMock(PlaylistService.class);
        playlistController = new PlaylistController(service);
        
    }


    @Test
    public void savePlaylistTest()
    {

        
        Capture<Playlist> capturedArg = EasyMock.newCapture();
        
        EasyMock.expect(service.savePlaylist(EasyMock.capture(capturedArg))).andReturn(playlist);
        EasyMock.replay(service);

        ResponseEntity<ResponseObject> response = playlistController.savePlaylist(playlistMapper, playlistMapper.getId());
       
        assertEquals(response.getBody().getStatus(), STATUS.SUCCESS);
        assertEquals(response.getBody().getMsg(), "Your playlist was saved");
        assertEquals(response.getBody().getObj(), playlist);
   
        EasyMock.verify(service);
        
        assertTrue(capturedArg.getValue().deeplyEquals(playlist));
    }

    @Test
    public void deletePlaylistTest()
    {
        Capture<Integer> capturedArg = EasyMock.newCapture();
        
        EasyMock.expect(service.deletePlaylist(EasyMock.captureInt(capturedArg))).andReturn(true);
        EasyMock.replay(service);

        ResponseEntity<ResponseObject> response = playlistController.deletePlaylist(playlistMapper.getId());
       
        assertEquals(response.getBody().getStatus(), STATUS.SUCCESS);
        assertEquals(response.getBody().getMsg(), "Your playlist was deleted");
        assertEquals(response.getBody().getObj(), true);
   
        EasyMock.verify(service);
        
        assertEquals(capturedArg.getValue(), playlistMapper.getId());
 
        
    }
    
 
    @Test
    public void getPlaylistsByUser()
    {
        Capture<Integer> capturedArg = EasyMock.newCapture();
        
        EasyMock.expect(service.getPlaylistsByUser(EasyMock.captureInt(capturedArg))).andReturn(playlists);
        EasyMock.replay(service);

        ResponseEntity<ResponseObject> response = playlistController.getPlaylistsByUserId(testUser.getId());
       
        assertEquals(response.getBody().getStatus(), STATUS.SUCCESS);
        assertEquals(response.getBody().getMsg(), "Come get your playlists");
        assertEquals(response.getBody().getObj(), playlists);
   
        EasyMock.verify(service);
        
        assertTrue(capturedArg.getValue().equals(testUser.getId()));
 
    }
    

    @Test
    public void getPlaylistsById()
    {
        Capture<Integer> capturedArg = EasyMock.newCapture();
        
        EasyMock.expect(service.getPlaylistById(EasyMock.captureInt(capturedArg))).andReturn(playlist);
        EasyMock.replay(service);

        ResponseEntity<ResponseObject> response = playlistController.getPlaylistById(playlistMapper.getId());
       
        assertEquals(response.getBody().getStatus(), STATUS.SUCCESS);
        assertEquals(response.getBody().getMsg(), "Here's your selected playlist");
        assertEquals(response.getBody().getObj(), playlist);
   
        EasyMock.verify(service);
        
        assertTrue(capturedArg.getValue().equals(playlistMapper.getId()));
 
    }

}
