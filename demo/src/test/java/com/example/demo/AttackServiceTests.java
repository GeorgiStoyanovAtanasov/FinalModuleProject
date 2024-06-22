package com.example.demo;

import com.example.demo.Entities.Attack;
import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Materials.Crystal;
import com.example.demo.Entities.Materials.Gold;
import com.example.demo.Entities.Materials.Silver;
import com.example.demo.Entities.Player;
import com.example.demo.Repositories.*;
import com.example.demo.Services.AttackService;
import com.example.demo.Services.TransactionalService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AttackServiceTests {
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private Model model;
    @Mock
    private ArcherRepository archerRepository;

    @Mock
    private SwordsmanRepository swordsmanRepository;

    @Mock
    private CavalryRepository cavalryRepository;
    @Mock
    private AttackRepository attackRepository;
    @Mock
    private TransactionalService transactionalService;

    @InjectMocks
    private AttackService attackService;

    @Test
    public void testAttackFrom() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");


        Player examplePlayer = new Player();
        examplePlayer.setUsername("testUser");


        when(playerRepository.findByUsername("testUser")).thenReturn(examplePlayer);

        List<Player> examplePlayers = new ArrayList<>();


        when(playerRepository.findAll()).thenReturn(examplePlayers);


        String result = attackService.attackFrom(model);


        assertEquals("choose-player-to-attack", result);


        verify(model).addAttribute("players", examplePlayers);
    }

    @Test
    public void testAttack_Valid() {

        Long playerToAttackId = 1L;
        int numberOfArchers = 2;
        int numberOfSwordsmen = 2;
        int numberOfCavalries = 2;


        Player player = new Player();
        when(playerRepository.findByUsername("testUser")).thenReturn(player);
        Player playerToAttack = new Player();
        when(playerRepository.findById(playerToAttackId)).thenReturn(Optional.of(playerToAttack));


        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testUser");


        List<Archer> archers = new ArrayList<>();
        for (int i = 0; i < numberOfArchers; i++) {
            archers.add(new Archer());
        }
        when(archerRepository.findAllByPlayerAndInBattle(player, false)).thenReturn(archers);

        List<Swordsman> swordsmen = new ArrayList<>();
        for (int i = 0; i < numberOfSwordsmen; i++) {
            swordsmen.add(new Swordsman());
        }
        when(swordsmanRepository.findAllByPlayerAndInBattle(player, false)).thenReturn(swordsmen);

        List<Cavalry> cavalries = new ArrayList<>();
        for (int i = 0; i < numberOfCavalries; i++) {
            cavalries.add(new Cavalry());
        }
        when(cavalryRepository.findAllByPlayerAndInBattle(player, false)).thenReturn(cavalries);


        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);


        String result = attackService.attack(playerToAttackId, numberOfArchers, numberOfSwordsmen, numberOfCavalries, redirectAttributes);


        assertEquals("attack-success", result);


        verify(playerRepository, times(2)).save(any(Player.class));


        verify(attackRepository).save(any(Attack.class));
    }

    @Test
    public void testCalculateAttackStrength() {

        Attack attack = mock(Attack.class);


        when(attack.getArchersUsed()).thenReturn(List.of(new Archer(), new Archer()));
        when(attack.getSwordsmenUsed()).thenReturn(List.of(new Swordsman()));
        when(attack.getCavalriesUsed()).thenReturn(List.of(new Cavalry(), new Cavalry(), new Cavalry()));


        AttackService attackService = new AttackService();


        int attackStrength = attackService.calculateAttackStrength(attack);


        assertEquals(2 * Archer.value + 1 * Swordsman.value + 3 * Cavalry.value, attackStrength);
    }

    @Test
    public void testAllPositiveNumbers() {
        AttackService attackService = new AttackService();
        int[] result = attackService.adjustNumbers(2, 3, 4, Archer.value, Swordsman.value, Cavalry.value);
        assertArrayEquals(new int[]{2, 3, 4}, result);
    }

    @Test
    public void testAllNegativeNumbers() {
        AttackService attackService = new AttackService();
        int[] result = attackService.adjustNumbers(-2, -3, -4, Archer.value, Swordsman.value, Cavalry.value);
        assertArrayEquals(new int[]{-2, -3, -4}, result);
    }

    @Test
    public void testSumPositiveNumbers() {
        AttackService attackService = new AttackService();
        int[] result = attackService.adjustNumbers(2, -3, 4, Archer.value, Swordsman.value, Cavalry.value);
        assertArrayEquals(new int[]{0, 0, 3}, result);
    }

    @Test
    public void testSumNegativeNumbers() {
        AttackService attackService = new AttackService();
        int[] result = attackService.adjustNumbers(-2, 3, -4, Archer.value, Swordsman.value, Cavalry.value);
        assertArrayEquals(new int[]{0, -5, 0}, result);
    }

    @Test
    public void testCalculateTotalDefenderValue() {

        Player defender = new Player();
        Attack attack = new Attack();
        attack.setDefender(defender);

        when(archerRepository.findAllByPlayerAndInBattle(defender, false)).thenReturn(Arrays.asList(new Archer(), new Archer()));
        when(swordsmanRepository.findAllByPlayerAndInBattle(defender, false)).thenReturn(Arrays.asList(new Swordsman()));
        when(cavalryRepository.findAllByPlayerAndInBattle(defender, false)).thenReturn(Arrays.asList(new Cavalry(), new Cavalry()));


        int totalDefenderValue = attackService.calculateTotalDefenderValue(attack);


        int expectedTotalDefenderValue = 2 * Archer.value + 1 * Swordsman.value + 2 * Cavalry.value;


        assertEquals(expectedTotalDefenderValue, totalDefenderValue);
    }
    @Test
    public void testDefendForm_NoAttack() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Model model = mock(Model.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("defenderUsername");
        Player defenderPlayer = new Player();
        when(playerRepository.findByUsername("defenderUsername")).thenReturn(defenderPlayer);
        when(attackRepository.findByDefender(defenderPlayer)).thenReturn(null);


        String result = attackService.defendForm(model);


        verify(playerRepository).save(defenderPlayer);
        assertSame("redirect:/home", result);
    }
    @Test
    public void testDefendForm_WithAttack() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("defenderUsername");
        Player defenderPlayer = new Player();
        when(playerRepository.findByUsername("defenderUsername")).thenReturn(defenderPlayer);


        Attack attack = new Attack();
        attack.setAttacker(new Player());
        attack.setArchersUsed(new ArrayList<>());
        attack.setSwordsmenUsed(new ArrayList<>());
        attack.setCavalriesUsed(new ArrayList<>());
        attack.setDefender(defenderPlayer);
        when(attackRepository.findByDefender(defenderPlayer)).thenReturn(attack);

        when(archerRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(Arrays.asList(new Archer(), new Archer()));
        when(swordsmanRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(Arrays.asList(new Swordsman()));
        when(cavalryRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(Arrays.asList(new Cavalry(), new Cavalry())); // Total defender value greater than attack strength


        String result = attackService.defendForm(model);


        verify(playerRepository).save(attack.getAttacker());
        verify(playerRepository).save(attack.getDefender());
        verify(model).addAttribute(eq("attack"), any(Attack.class));
        verify(model).addAttribute("attackerUsername", attack.getAttacker().getUsername());
        verify(model).addAttribute("archers", attack.getArchersUsed().size());
        verify(model).addAttribute("swordsmen", attack.getSwordsmenUsed().size());
        verify(model).addAttribute("cavalries", attack.getCavalriesUsed().size());
        verify(archerRepository, times(2)).findAllByPlayerAndInBattle(defenderPlayer, false);
        verify(swordsmanRepository, times(2)).findAllByPlayerAndInBattle(defenderPlayer, false);
        verify(cavalryRepository, times(2)).findAllByPlayerAndInBattle(defenderPlayer, false);
        assertEquals("defend", result);
    }
    @Test
    public void testTransferResources() {

        Attack attack = new Attack();
        Player attacker = new Player();
        attacker.setUsername("attackerUsername");
        Player defender = new Player();
        Gold attackerGold = new Gold();
        Gold defenderGold = new Gold();
        Silver attackerSilver = new Silver();
        Silver defenderSilver = new Silver();
        Crystal attackerCrystal = new Crystal();
        Crystal defenderCrystal = new Crystal();
        attacker.setGold(attackerGold);
        defender.setGold(defenderGold);
        attacker.setSilver(attackerSilver);
        defender.setSilver(defenderSilver);
        attacker.setCrystal(attackerCrystal);
        defender.setCrystal(defenderCrystal);
        defender.setArcherWorkshops(new ArrayList<>());
        defender.setSwordsmanWorkshops(new ArrayList<>());
        defender.setCavalryWorkshops(new ArrayList<>());
        attackerGold.setAmount(100);
        defenderGold.setAmount(50);
        attackerSilver.setAmount(200);
        defenderSilver.setAmount(100);
        attackerCrystal.setAmount(300);
        defenderCrystal.setAmount(150);
        attack.setAttacker(attacker);
        attack.setDefender(defender);
        List<Archer> archersUsed = new ArrayList<>();
        List<Swordsman> swordsmenUsed = new ArrayList<>();
        List<Cavalry> cavalriesUsed = new ArrayList<>();
        archersUsed.add(new Archer());
        swordsmenUsed.add(new Swordsman());
        swordsmenUsed.add(new Swordsman());
        cavalriesUsed.add(new Cavalry());
        cavalriesUsed.add(new Cavalry());
        cavalriesUsed.add(new Cavalry());
        attack.setArchersUsed(archersUsed);
        attack.setSwordsmenUsed(swordsmenUsed);
        attack.setCavalriesUsed(cavalriesUsed);



        String result = attackService.transferResources(attack, 1, 2, 3, model);


        assertEquals(150, attacker.getGold().getAmount());
        assertEquals(0, defender.getGold().getAmount());
        assertEquals(300, attacker.getSilver().getAmount());
        assertEquals(0, defender.getSilver().getAmount());
        assertEquals(450, attacker.getCrystal().getAmount());
        assertEquals(0, defender.getCrystal().getAmount());



        verify(playerRepository, times(2)).save(any(Player.class));
        verify(archerRepository, times(1)).findAllByPlayer(any(Player.class));
        verify(swordsmanRepository, times(1)).findAllByPlayer(any(Player.class));
        verify(cavalryRepository, times(1)).findAllByPlayer(any(Player.class));


        assertEquals("defeat", result);
    }
    @Test
    public void testDefendAndBattleWhenValid() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Model model = mock(Model.class);
        Player defenderPlayer = new Player();
        Attack attack = new Attack();
        attack.setArchersUsed(new ArrayList<>());
        attack.setCavalriesUsed(new ArrayList<>());
        attack.setSwordsmenUsed(new ArrayList<>());
        List<Archer> mockArchers = new ArrayList<>();
        List<Swordsman> mockSwordsmen = new ArrayList<>();
        List<Cavalry> mockCavalries = new ArrayList<>();

        Archer archer1 = new Archer();

        mockArchers.add(archer1);

        Archer archer2 = new Archer();

        mockArchers.add(archer2);
        for (int i = 0; i < 4; i++) {
            Swordsman swordsman = new Swordsman(defenderPlayer);

            mockSwordsmen.add(swordsman);
        }


        for (int i = 0; i < 5; i++) {
            Cavalry cavalry = new Cavalry(defenderPlayer);

            mockCavalries.add(cavalry);
        }

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("defenderUsername");
        when(playerRepository.findByUsername("defenderUsername")).thenReturn(defenderPlayer);
        when(attackRepository.findByDefender(defenderPlayer)).thenReturn(attack);
        when(archerRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(mockArchers);
        when(swordsmanRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(mockSwordsmen);
        when(cavalryRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(mockCavalries);

        String result = attackService.defend(2, 3, 4, model, redirectAttributes);


        verify(playerRepository, times(1)).save(defenderPlayer);
        verify(archerRepository, times(5)).findAllByPlayerAndInBattle(defenderPlayer, false);
        verify(swordsmanRepository, times(7)).findAllByPlayerAndInBattle(defenderPlayer, false);
        verify(cavalryRepository, times(9)).findAllByPlayerAndInBattle(defenderPlayer, false);
        verify(redirectAttributes, never()).addFlashAttribute(anyString(), anyString());



        assertEquals("redirect:/home", result);
    }
    @Test
    public void testDefendAndBattleWhenInvalid() {

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Model model = mock(Model.class);
        Player defenderPlayer = new Player();
        Attack attack = new Attack();
        attack.setArchersUsed(new ArrayList<>());
        attack.setCavalriesUsed(new ArrayList<>());
        attack.setSwordsmenUsed(new ArrayList<>());
        for (int i = 0; i < 3; i++) {
            Archer archer = new Archer(attack.getAttacker());

            attack.getArchersUsed().add(archer);
        }


        for (int i = 0; i < 5; i++) {
            Swordsman swordsman = new Swordsman(attack.getAttacker());

            attack.getSwordsmenUsed().add(swordsman);
        }


        for (int i = 0; i < 9; i++) {
            Cavalry cavalry = new Cavalry(attack.getAttacker());

            attack.getCavalriesUsed().add(cavalry);
        }
        List<Archer> mockArchers = new ArrayList<>();
        List<Swordsman> mockSwordsmen = new ArrayList<>();
        List<Cavalry> mockCavalries = new ArrayList<>();

        Archer archer1 = new Archer();

        mockArchers.add(archer1);

        Archer archer2 = new Archer();

        mockArchers.add(archer2);
        for (int i = 0; i < 4; i++) {
            Swordsman swordsman = new Swordsman(defenderPlayer);

            mockSwordsmen.add(swordsman);
        }


        for (int i = 0; i < 5; i++) {
            Cavalry cavalry = new Cavalry(defenderPlayer);

            mockCavalries.add(cavalry);
        }
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("defenderUsername");
        when(playerRepository.findByUsername("defenderUsername")).thenReturn(defenderPlayer);
        when(attackRepository.findByDefender(defenderPlayer)).thenReturn(attack);
        when(archerRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(mockArchers);
        when(swordsmanRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(mockSwordsmen);
        when(cavalryRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(mockCavalries);

        String result = attackService.defend(2, 3, 4, model, redirectAttributes);


        verify(playerRepository, times(0)).save(defenderPlayer);
        verify(archerRepository, times(5)).findAllByPlayerAndInBattle(defenderPlayer, false);
        verify(swordsmanRepository, times(7)).findAllByPlayerAndInBattle(defenderPlayer, false);
        verify(cavalryRepository, times(9)).findAllByPlayerAndInBattle(defenderPlayer, false);
        verify(redirectAttributes, times(1)).addFlashAttribute(anyString(), anyString());


        assertEquals("redirect:/defend", result);
    }
}
