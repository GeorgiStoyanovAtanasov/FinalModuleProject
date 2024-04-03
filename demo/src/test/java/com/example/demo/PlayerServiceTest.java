    package com.example.demo;

    import com.example.demo.Entities.Player;
    import com.example.demo.Repositories.CrystalRepository;
    import com.example.demo.Repositories.GoldRepository;
    import com.example.demo.Repositories.PlayerRepository;
    import com.example.demo.Repositories.SilverRepository;
    import com.example.demo.Services.PlayerService;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;
    import org.springframework.validation.BindingResult;

    import static org.mockito.Mockito.*;

    public class PlayerServiceTest {

        @Mock
        private PlayerRepository playerRepository;

        @Mock
        private GoldRepository goldRepository;

        @Mock
        private SilverRepository silverRepository;

        @Mock
        private CrystalRepository crystalRepository;

        @InjectMocks
        private PlayerService playerService;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testRegisterPlayerSuccess() {
            Player player = new Player();
            player.setUsername("testUser");
            player.setPassword("testPassword");

            when(playerRepository.findByUsername(anyString())).thenReturn(null);

            BindingResult bindingResult = null;
            String result = playerService.registerPlayer(player,bindingResult);
            verify(playerRepository, times(1)).save(any(Player.class));
        }
    }

