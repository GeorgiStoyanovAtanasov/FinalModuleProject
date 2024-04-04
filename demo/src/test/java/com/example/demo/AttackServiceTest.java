package com.example.demo;

import com.example.demo.Entities.Attack;
import com.example.demo.Entities.Fighters.Archer;
import com.example.demo.Entities.Fighters.Cavalry;
import com.example.demo.Entities.Fighters.Swordsman;
import com.example.demo.Entities.Player;
import com.example.demo.Entities.Workshops.ArcherWorkshop;
import com.example.demo.Entities.Workshops.CavalryWorkshop;
import com.example.demo.Entities.Workshops.SwordsmanWorkshop;
import com.example.demo.Repositories.*;
import com.example.demo.Services.AttackService;
import com.example.demo.Services.TransactionalService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttackServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ArcherRepository archerRepository;

    @Mock
    private SwordsmanRepository swordsmanRepository;

    @Mock
    private CavalryRepository cavalryRepository;
    @Mock
    private Model model;
    @Mock
    private AttackRepository attackRepository;
    @Mock
    private Archer archer;

    @Mock
    private Swordsman swordsman;

    @Mock
    private Cavalry cavalry;
    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContextHolder securityContextHolder;
    @Mock
    private TransactionalService transactionalService;


    @InjectMocks
    private AttackService attackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAttackFrom() {
        // Mock Authentication and player
        Player mockPlayer = new Player();
        mockPlayer.setUsername("testUser");

        // Mock players list
        List<Player> mockPlayers = new ArrayList<>();
        Player player1 = new Player();
        player1.setUsername("player1");
        Player player2 = new Player();
        player2.setUsername("player2");
        mockPlayers.add(player1);
        mockPlayers.add(player2);

        // Mock playerRepository behavior
        when(playerRepository.findByUsername(anyString())).thenReturn(mockPlayer);
        when(playerRepository.findAll()).thenReturn(mockPlayers);

        // Mock model behavior
        List<Player> expectedSelectedPlayers = new ArrayList<>();
        expectedSelectedPlayers.add(player1);
        expectedSelectedPlayers.add(player2);
        when(model.addAttribute(eq("players"), anyList())).thenReturn(model);

        // Invoke the method under test
        String viewName = attackService.attackFrom(model);

        // Verify interactions and assertions
        assertEquals("choose-player-to-attack", viewName);
        verify(model).addAttribute("players", expectedSelectedPlayers);
        verify(playerRepository).findByUsername("testUser");
        verify(playerRepository).findAll();
    }
    @Test
    @WithMockUser(roles = "USER")
    void testAttack_PlayerNotFound() {
        Long playerToAttackId = 1L;
        when(playerRepository.findById(playerToAttackId)).thenReturn(null);

        String result = attackService.attack(playerToAttackId, 3, 2, 1);

        assertEquals("choose-player-to-attack", result);
        verify(playerRepository).findById(playerToAttackId);
        verifyNoInteractions(archerRepository, swordsmanRepository, cavalryRepository);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAttack_InsufficientUnits() {
        Long playerToAttackId = 1L;
        Player playerToAttack = new Player();
        when(playerRepository.findById(playerToAttackId)).thenReturn(Optional.of(playerToAttack));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("attacker");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Player attacker = new Player();
        when(playerRepository.findByUsername("attacker")).thenReturn(attacker);

        when(archerRepository.findAllByPlayerAndInBattle(attacker, false)).thenReturn(List.of(new Archer(), new Archer()));
        when(swordsmanRepository.findAllByPlayerAndInBattle(attacker, false)).thenReturn(List.of(new Swordsman()));
        when(cavalryRepository.findAllByPlayerAndInBattle(attacker, false)).thenReturn(List.of());

        String result = attackService.attack(playerToAttackId, 3, 2, 1);

        assertEquals("choose-player-to-attack", result);
        verify(playerRepository).findById(playerToAttackId);
        verify(playerRepository).findByUsername("attacker");
        verify(archerRepository).findAllByPlayerAndInBattle(attacker, false);
        verify(swordsmanRepository).findAllByPlayerAndInBattle(attacker, false);
        verify(cavalryRepository).findAllByPlayerAndInBattle(attacker, false);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAttack_SuccessfulAttack() {
        Long playerToAttackId = 1L;
        Player playerToAttack = new Player();
        when(playerRepository.findById(playerToAttackId)).thenReturn(Optional.of(playerToAttack));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("attacker");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Player attacker = new Player();
        when(playerRepository.findByUsername("attacker")).thenReturn(attacker);

        when(archerRepository.findAllByPlayerAndInBattle(attacker, false)).thenReturn(List.of(new Archer(), new Archer()));
        when(swordsmanRepository.findAllByPlayerAndInBattle(attacker, false)).thenReturn(List.of(new Swordsman()));
        when(cavalryRepository.findAllByPlayerAndInBattle(attacker, false)).thenReturn(List.of(new Cavalry()));

        String result = attackService.attack(playerToAttackId, 2, 1, 1);

        assertEquals("attack-success", result);
        verify(playerRepository).findById(playerToAttackId);
        verify(playerRepository).findByUsername("attacker");
        verify(archerRepository).findAllByPlayerAndInBattle(attacker, false);
        verify(swordsmanRepository).findAllByPlayerAndInBattle(attacker, false);
        verify(cavalryRepository).findAllByPlayerAndInBattle(attacker, false);
    }
    @Test
    @WithMockUser(roles = "USER")
    void testAttackPart2_SuccessfulAttack() {
        Player attacker = new Player();
        Player playerToBeAttacked = new Player();
        List<Archer> archersToAttackWith = new ArrayList<>();
        archersToAttackWith.add(new Archer());
        archersToAttackWith.add(new Archer());
        List<Swordsman> swordsmenToAttackWith = new ArrayList<>();
        swordsmenToAttackWith.add(new Swordsman());
        List<Cavalry> cavalriesToAttackWith = new ArrayList<>();
        cavalriesToAttackWith.add(new Cavalry());

        // Mock the behavior of repositories
        when(archerRepository.findAllByPlayerAndInBattle(attacker, false)).thenReturn(archersToAttackWith);
        when(swordsmanRepository.findAllByPlayerAndInBattle(attacker, false)).thenReturn(swordsmenToAttackWith);
        when(cavalryRepository.findAllByPlayerAndInBattle(attacker, false)).thenReturn(cavalriesToAttackWith);

        // Execute the attackPart2 method
        String result = attackService.attackPart2(attacker, playerToBeAttacked, 2, 1, 1);

        // Verify repository interactions
        verify(playerRepository).save(playerToBeAttacked);
        verify(playerRepository).save(attacker);
        verify(attackRepository).save(any(Attack.class)); // We verify that any instance of Attack is saved

        // Assertions
        assertEquals("attack-success", result);
        assertEquals(true, playerToBeAttacked.isAttacked());
        for (Archer archer : archersToAttackWith) {
            assertEquals(true, archer.isInBattle());
        }
        for (Swordsman swordsman : swordsmenToAttackWith) {
            assertEquals(true, swordsman.isInBattle());
        }
        for (Cavalry cavalry : cavalriesToAttackWith) {
            assertEquals(true, cavalry.isInBattle());
        }
    }
    @Test
    @WithMockUser(roles = "USER")
    void testCalculateAttackStrength() {
        // Mock Attack object with lists of fighters
        Attack attack = new Attack();
        List<Archer> archersUsed = Arrays.asList(mock(Archer.class), mock(Archer.class)); // Mock Archer objects
        List<Swordsman> swordsmenUsed = Arrays.asList(mock(Swordsman.class)); // Mock Swordsman objects
        List<Cavalry> cavalriesUsed = Arrays.asList(mock(Cavalry.class), mock(Cavalry.class), mock(Cavalry.class)); // Mock Cavalry objects
        attack.setArchersUsed(archersUsed);
        attack.setSwordsmenUsed(swordsmenUsed);
        attack.setCavalriesUsed(cavalriesUsed);

        // Mock values for Archer, Swordsman, and Cavalry strength
        when(archer.getValue()).thenReturn(10); // Mock Archer value
        when(swordsman.getValue()).thenReturn(15); // Mock Swordsman value
        when(cavalry.getValue()).thenReturn(20); // Mock Cavalry value

        // Execute the calculateAttackStrength method
        int attackStrength = attackService.calculateAttackStrength(attack);

        // Verify that getValue() is called for each fighter used in the attack
        verify(archer, times(2)).getValue(); // Archers are used twice
        verify(swordsman, times(1)).getValue(); // One Swordsman is used
        verify(cavalry, times(3)).getValue(); // Three Cavalries are used

        // Calculate expected attack strength based on mocked values
        int expectedStrength = (2 * 10) + (1 * 15) + (3 * 20); // Archers + Swordsman + Cavalries

        // Assert that the calculated attack strength matches the expected value
        assertEquals(expectedStrength, attackStrength);
    }
    @Test
    @WithMockUser(roles = "USER")
    void testAdjustNumbers_Negative1_Positive2_Positive3() {
        int num1 = -1, num2 = 2, num3 = 3;
        int value1 = 10, value2 = 20, value3 = 30;

        int[] adjustedNumbers = attackService.adjustNumbers(num1, num2, num3, value1, value2, value3);

        // Validate the expected output after adjusting numbers
        assertArrayEquals(new int[]{0, 0, 5}, adjustedNumbers);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAdjustNumbers_Positive1_Negative2_Positive3() {
        int num1 = 1, num2 = -2, num3 = 3;
        int value1 = 10, value2 = 20, value3 = 30;

        int[] adjustedNumbers = attackService.adjustNumbers(num1, num2, num3, value1, value2, value3);

        // Validate the expected output after adjusting numbers
        assertArrayEquals(new int[]{0, 0, 2}, adjustedNumbers);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAdjustNumbers_Positive1_Positive2_Negative3() {
        int num1 = 1, num2 = 2, num3 = -3;
        int value1 = 10, value2 = 20, value3 = 30;

        int[] adjustedNumbers = attackService.adjustNumbers(num1, num2, num3, value1, value2, value3);

        // Validate the expected output after adjusting numbers
        assertArrayEquals(new int[]{1, 0, 0}, adjustedNumbers);
    }
    @Test
    @WithMockUser(roles = "USER")
    void testCalculateTotalDefenderValue() {
        // Create a mock defender
        Player defender = new Player();
        defender.setId(1L);

        // Mock the behavior of repository methods
        when(archerRepository.findAllByPlayerAndInBattle(defender, false)).thenReturn(Collections.singletonList(new Archer()));
        when(swordsmanRepository.findAllByPlayerAndInBattle(defender, false)).thenReturn(Collections.singletonList(new Swordsman()));
        when(cavalryRepository.findAllByPlayerAndInBattle(defender, false)).thenReturn(Collections.singletonList(new Cavalry()));

        // Create an attack with the mocked defender
        Attack attack = new Attack();
        attack.setDefender(defender);

        // Call the method under test
        int totalDefenderValue = attackService.calculateTotalDefenderValue(attack);

        // Calculate the expected defender strength based on the mocked units
        int expectedDefenderStrength = (1 * Archer.value) + (1 * Swordsman.value) + (1 * Cavalry.value);

        // Verify that the method returns the expected defender strength
        assertEquals(expectedDefenderStrength, totalDefenderValue);

        // Verify repository method calls
        verify(archerRepository, times(1)).findAllByPlayerAndInBattle(defender, false);
        verify(swordsmanRepository, times(1)).findAllByPlayerAndInBattle(defender, false);
        verify(cavalryRepository, times(1)).findAllByPlayerAndInBattle(defender, false);
    }
    @Test
    @WithMockUser(roles = "USER")
    void testDefendForm_AttackIsNull() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Attack attack = null;

        // Mock SecurityContextHolder behavior
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock player repository behavior
        Player defenderPlayer = new Player();
        defenderPlayer.setUsername("defender");
        when(playerRepository.findByUsername("defender")).thenReturn(defenderPlayer);

        // Mock attack repository behavior
        when(attackRepository.findByDefender(defenderPlayer)).thenReturn(attack);

        // Act
        String result = attackService.defendForm(mock(Model.class));

        // Assert
        assertEquals("redirect:/home", result);
        assertFalse(defenderPlayer.isAttacked());
    }
    @Test
    @WithMockUser(roles = "USER")
    void testTransferResources() {
        // Arrange
        Player attacker = new Player();
        attacker.setUsername("attacker");
//        attacker.setGold(new Resource(100));
//        attacker.setSilver(new Resource(50));
//        attacker.setCrystal(new Resource(20));

        Player defender = new Player();
        defender.setUsername("defender");
//        defender.setGold(new Resource(50));
//        defender.setSilver(new Resource(25));
//        defender.setCrystal(new Resource(10));

        List<ArcherWorkshop> defenderArcherWorkshops = new ArrayList<>();
        List<SwordsmanWorkshop> defenderSwordsmanWorkshops = new ArrayList<>();
        List<CavalryWorkshop> defenderCavalryWorkshops = new ArrayList<>();

        Attack attack = new Attack(attacker, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), defender);
        attack.getDefender().setArcherWorkshops(defenderArcherWorkshops);
        attack.getDefender().setSwordsmanWorkshops(defenderSwordsmanWorkshops);
        attack.getDefender().setCavalryWorkshops(defenderCavalryWorkshops);

        Model model = mock(Model.class);

        // Mock repository behaviors
        when(playerRepository.save(attacker)).thenReturn(attacker);
        when(playerRepository.save(defender)).thenReturn(defender);
        when(archerRepository.findAllByPlayer(defender)).thenReturn(new ArrayList<>());
        when(swordsmanRepository.findAllByPlayer(defender)).thenReturn(new ArrayList<>());
        when(cavalryRepository.findAllByPlayer(defender)).thenReturn(new ArrayList<>());

        // Act
        String result = attackService.transferResources(attack, 0, 0, 0, model);

        // Assert
        assertEquals("defeat", result);
        assertEquals(150, attacker.getGold().getAmount());
        assertEquals(75, attacker.getSilver().getAmount());
        assertEquals(30, attacker.getCrystal().getAmount());
        assertEquals(0, defender.getGold().getAmount());
        assertEquals(0, defender.getSilver().getAmount());
        assertEquals(0, defender.getCrystal().getAmount());

        verify(playerRepository, times(2)).save(any(Player.class));
        verify(attackRepository, times(1)).delete(attack);
//        verify(transactionalService, times(1)).deleteOnlyTheEntitiesOfAnAttackThatHasBeenCountered(any(), any(), any());
        verify(model, times(1)).addAttribute(eq("player"), eq(attacker.getUsername()));
    }
    @Test
    @WithMockUser(roles = "USER")
    void testDefend() {
        // Arrange
        Player defenderPlayer = new Player();
        defenderPlayer.setUsername("defender");

        Attack attack = new Attack();
        List<Archer> archersToDefendWith = new ArrayList<>();
        List<Swordsman> swordsmenToDefendWith = new ArrayList<>();
        List<Cavalry> cavalriesToDefendWith = new ArrayList<>();

        Model model = mock(Model.class);

        MockitoAnnotations.openMocks(this);

        when(securityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(defenderPlayer.getUsername());
        when(playerRepository.findByUsername(defenderPlayer.getUsername())).thenReturn(defenderPlayer);
        when(attackRepository.findByDefender(defenderPlayer)).thenReturn(attack);
        when(archerRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(new ArrayList<>());
        when(swordsmanRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(new ArrayList<>());
        when(cavalryRepository.findAllByPlayerAndInBattle(defenderPlayer, false)).thenReturn(new ArrayList<>());

        // Act
        String result = attackService.defend(0, 0, 0, model);

        // Assert
        assertEquals("redirect:/home", result);
        assertFalse(defenderPlayer.isAttacked());
        verify(playerRepository, times(1)).save(defenderPlayer);
        verify(model, never()).addAttribute(anyString(), any());
        verify(archerRepository, never()).findAllByPlayerAndInBattle(any(), anyBoolean());
        verify(swordsmanRepository, never()).findAllByPlayerAndInBattle(any(), anyBoolean());
        verify(cavalryRepository, never()).findAllByPlayerAndInBattle(any(), anyBoolean());
        verify(attackService, never()).battle(anyList(), anyList(), anyList(), any(), any());
    }
    @Test
    void testBattle() {
        // Arrange
        List<Archer> archersToDefendWith = new ArrayList<>();
        List<Swordsman> swordsmenToDefendWith = new ArrayList<>();
        List<Cavalry> cavalriesToDefendWith = new ArrayList<>();
        Attack attack = new Attack();
        Player defenderPlayer = new Player();

        MockitoAnnotations.openMocks(this);

        // Mocking repository behavior
        when(archerRepository.findAllByPlayerAndInBattle(any(), anyBoolean())).thenReturn(new ArrayList<>());
        when(swordsmanRepository.findAllByPlayerAndInBattle(any(), anyBoolean())).thenReturn(new ArrayList<>());
        when(cavalryRepository.findAllByPlayerAndInBattle(any(), anyBoolean())).thenReturn(new ArrayList<>());

        // Mocking attack and defender player
        attack.setArchersUsed(new ArrayList<>());
        attack.setSwordsmenUsed(new ArrayList<>());
        attack.setCavalriesUsed(new ArrayList<>());

        archersToDefendWith.add(new Archer());
        swordsmenToDefendWith.add(new Swordsman());
        cavalriesToDefendWith.add(new Cavalry());

        // Act
        String result = attackService.battle(archersToDefendWith, swordsmenToDefendWith, cavalriesToDefendWith, attack, defenderPlayer);

        // Assert
        assertEquals("redirect:/home", result);
        verify(archerRepository, never()).findAllByPlayerAndInBattle(any(), anyBoolean());
        verify(swordsmanRepository, never()).findAllByPlayerAndInBattle(any(), anyBoolean());
        verify(cavalryRepository, never()).findAllByPlayerAndInBattle(any(), anyBoolean());
        verify(attackRepository, times(1)).delete(attack);
        verify(transactionalService, times(1)).deleteOnlyTheEntitiesOfAnAttackThatHasBeenCountered(any(), any(), any());
        assertFalse(defenderPlayer.isAttacked());
        verify(playerRepository, times(1)).save(defenderPlayer);
        verify(playerRepository, times(1)).save(attack.getAttacker());
    }
}


