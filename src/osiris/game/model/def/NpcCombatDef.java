package osiris.game.model.def;

/*
 * Osiris Emulator
 * Copyright (C) 2011  Garrett Woodard, Blake Beaupain, Travis Burtrum
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 * 
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import osiris.game.model.combat.Attack;
import osiris.game.model.combat.HitType;
import osiris.game.model.combat.impl.MagicAttack;
import osiris.game.model.effect.CombatEffect;
import osiris.game.model.magic.SpellBook;

// TODO: Auto-generated Javadoc
/**
 * The Class NpcCombatDef.
 * 
 * @author Boomer
 */
public class NpcCombatDef {

	/** The combat defs. */
	private static HashMap<Integer, NpcCombatDef> combatDefs;

	/** The death animation. */
	private int maxHealth = 0, blockAnimation = 424, deathAnimation = 7197;

	/** The attacks. */
	private Attack[] attacks = { new Attack() };

	/**
	 * Instantiates a new npc combat def.
	 */
	private NpcCombatDef() {
	}

	/**
	 * Instantiates a new npc combat def.
	 * 
	 * @param maxHealth
	 *            the max health
	 * @param attacks
	 *            the attacks
	 * @param blockAnimation
	 *            the block animation
	 * @param deathAnimation
	 *            the death animation
	 */
	public NpcCombatDef(int maxHealth, Attack[] attacks, int blockAnimation, int deathAnimation) {
		this.maxHealth = maxHealth;
		this.attacks = attacks;
		this.blockAnimation = blockAnimation;
		this.deathAnimation = deathAnimation;
	}

	/**
	 * For id.
	 * 
	 * @param id
	 *            the id
	 * @return the npc combat def
	 */
	public static NpcCombatDef forId(int id) {
		NpcCombatDef existing = combatDefs.get(id);
		if (existing == null)
			return new NpcCombatDef();
		else
			return existing.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	protected NpcCombatDef clone() {
		return new NpcCombatDef(maxHealth, attacks, blockAnimation, deathAnimation);
	}

	/**
	 * Gets the attacks.
	 * 
	 * @return the attacks
	 */
	public Attack[] getAttacks() {
		return attacks;
	}

	/**
	 * Gets the attack.
	 * 
	 * @return the attack
	 */
	public Attack getAttack() {
		return attacks[new Random().nextInt(attacks.length)];
	}

	/**
	 * Gets the max hp.
	 * 
	 * @return the max hp
	 */
	public int getMaxHp() {
		return maxHealth;
	}

	/**
	 * Gets the block animation.
	 * 
	 * @return the block animation
	 */
	public int getBlockAnimation() {
		return blockAnimation;
	}

	/**
	 * Gets the death animation.
	 * 
	 * @return the death animation
	 */
	public int getDeathAnimation() {
		return deathAnimation;
	}

	/**
	 * Load.
	 */
	public static void load() {
		HashMap<Integer, NpcCombatDef> list = new HashMap<Integer, NpcCombatDef>();
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = f.newDocumentBuilder();
			Document doc = builder.parse(new File("./data/config/npc-combat.xml"));
			doc.getDocumentElement().normalize();

			NodeList defList = (NodeList) doc.getElementsByTagName("definition");
			if (defList == null)
				return;
			for (int i = 0; i < defList.getLength(); i++) {
				Element combatDef = (Element) defList.item(i);
				// Stuff to set
				List<Integer> npcIds = new ArrayList<Integer>();
				List<Attack> attacks = new ArrayList<Attack>();
				int health = 0, blockAnim = 424, deathAnim = 7197;

				Element npcIdsElement = (Element) combatDef.getElementsByTagName("ids").item(0);
				if (npcIdsElement != null) {
					String value = npcIdsElement.getFirstChild().getNodeValue();
					StringTokenizer st = new StringTokenizer(value, ",");
					while (st.hasMoreTokens()) {
						Integer id = Integer.parseInt(st.nextToken());
						npcIds.add(id);
					}
				}
				NodeList itemsList = (NodeList) combatDef.getElementsByTagName("info");
				for (int n = 0; n < itemsList.getLength(); n++) {
					Element itemElement = (Element) itemsList.item(n);
					health = Integer.parseInt(itemElement.getAttribute("health"));
					blockAnim = Integer.parseInt(itemElement.getAttribute("block"));
					deathAnim = Integer.parseInt(itemElement.getAttribute("death"));
				}

				NodeList magicList = (NodeList) combatDef.getElementsByTagName("spell");
				if (magicList != null) {
					for (int m = 0; m < magicList.getLength(); m++) {
						Element mageElement = (Element) magicList.item(m);
						int spell = Integer.parseInt(mageElement.getAttribute("id"));
						SpellBook spellBook = SpellBook.valueOf(mageElement.getAttribute("book").toUpperCase());
						attacks.add(new MagicAttack(spell, spellBook));
					}
				}
				NodeList customList = (NodeList) combatDef.getElementsByTagName("custom");
				if (customList != null) {
					for (int c = 0; c < customList.getLength(); c++) {
						Element customElement = (Element) customList.item(c);
						String className = "";
						int chance = 100;
						for (int a = 0; a < customElement.getAttributes().getLength(); a++) {
							Node node = customElement.getAttributes().item(a);
							String attribute = node.getNodeName();
							if (attribute.equalsIgnoreCase("class")) {
								className = node.getNodeValue();
							} else if (attribute.equalsIgnoreCase("chance")) {
								chance = Integer.parseInt(node.getNodeValue());
							}
						}
						Attack attack = Attack.create(className);
						attack.setChance(chance);
						attacks.add(attack);
					}
				}
				NodeList attacksList = (NodeList) combatDef.getElementsByTagName("attack");
				if (attacksList != null) {
					for (int att = 0; att < attacksList.getLength(); att++) {
						Attack attack = new Attack();
						Element attackElement = (Element) attacksList.item(att);
						for (int a = 0; a < attackElement.getAttributes().getLength(); a++) {
							Node node = attackElement.getAttributes().item(a);
							String attribute = node.getNodeName();
							if (attribute.equalsIgnoreCase("type")) {
								attack.setHitType(HitType.valueOf(node.getNodeValue().toUpperCase()));
							} else if (attribute.equalsIgnoreCase("delay")) {
								attack.setDelay(Integer.parseInt(node.getNodeValue()));
							} else if (attribute.equalsIgnoreCase("chance")) {
								attack.setChance(Integer.parseInt(node.getNodeValue()));
							} else if (attribute.equalsIgnoreCase("animation")) {
								int animations[];
								StringTokenizer st = new StringTokenizer(node.getNodeValue(), ",");
								animations = new int[st.countTokens()];
								int index = 0;
								while (st.hasMoreTokens()) {
									Integer id = Integer.parseInt(st.nextToken());
									animations[index] = id;
									index++;
								}
								attack.setAnimation(animations);
							} else if (attribute.equalsIgnoreCase("projectile")) {
								ArrayList<Integer[]> projectiles = new ArrayList<Integer[]>();
								StringTokenizer st = new StringTokenizer(node.getNodeValue(), ",");
								Integer[] projectile = new Integer[4];
								int index = 0;
								if (st.countTokens() == 1) {
									projectile[0] = Integer.parseInt(st.nextToken());
									projectile[1] = 90;
									projectile[2] = 46;
									projectile[3] = 0;
								} else
									while (st.hasMoreTokens()) {
										Integer id = Integer.parseInt(st.nextToken());
										projectile[index] = id;
										index++;
									}
								projectiles.add(projectile);
								int[][] projectilez = new int[projectiles.size()][];
								for (int p = 0; p < projectiles.size(); p++) {
									projectilez[p] = new int[projectiles.get(p).length];
									for (int p2 = 0; p2 < projectiles.get(p).length; p2++)
										projectilez[p][p2] = projectiles.get(p)[p2];
								}
								attack.setProjectiles(projectilez);
							} else if (attribute.equalsIgnoreCase("effect")) {
								StringTokenizer allEffects = new StringTokenizer(node.getNodeValue(), ";");
								while (allEffects.hasMoreTokens()) {
									StringTokenizer aEffect = new StringTokenizer(allEffects.nextToken(), ",");
									Object[] effect = new Object[aEffect.countTokens()];
									int index = 0;
									while (aEffect.hasMoreTokens()) {
										effect[index] = aEffect.nextToken();
										index++;
									}
									int effectIndex = Integer.parseInt((String) effect[0]);
									int effectChance = Integer.parseInt((String) effect[1]);
									Class<? extends CombatEffect> theClass = (Class<? extends CombatEffect>) Class.forName("osiris.game.model.effect." + effect[2]);
									int cooldown = Integer.parseInt((String) effect[3]);
									int amount = effect.length - 4;
									Object[] params = new Object[amount];
									System.arraycopy(effect, 4, params, 0, amount);
									attack.addEffect(effectIndex, effectChance, theClass, cooldown, params);
								}

							} else if (attribute.equalsIgnoreCase("attackgfx")) {
								int[] attackGraphic = new int[2];
								StringTokenizer st = new StringTokenizer(node.getNodeValue(), ",");
								int index = 0;
								while (st.hasMoreTokens() && index < 2) {
									Integer value = Integer.parseInt(st.nextToken());
									attackGraphic[index] = value;
									index++;
								}
								attack.setAttackGfx(attackGraphic);
							} else if (attribute.equalsIgnoreCase("hitgfx")) {
								int[] hitGraphic = new int[2];
								StringTokenizer st = new StringTokenizer(node.getNodeValue(), ",");
								int index = 0;
								while (st.hasMoreTokens() && index < 2) {
									Integer value = Integer.parseInt(st.nextToken());
									hitGraphic[index] = value;
									index++;
								}
								attack.setHitGfx(hitGraphic);
							} else if (attribute.equalsIgnoreCase("hit")) {
								ArrayList<Integer[]> hits = new ArrayList<Integer[]>();
								StringTokenizer allHits = new StringTokenizer(node.getNodeValue(), ";");
								while (allHits.hasMoreTokens()) {
									StringTokenizer aHit = new StringTokenizer(allHits.nextToken(), ",");
									Integer[] hit = new Integer[aHit.countTokens()];
									int index = 0;
									while (aHit.hasMoreTokens()) {
										Integer value = Integer.parseInt(aHit.nextToken());
										hit[index] = value;
										index++;
									}
									hits.add(hit);
								}
								int[][] hitz = new int[hits.size()][];
								for (int h = 0; h < hits.size(); h++) {
									hitz[h] = new int[hits.get(h).length];
									for (int h2 = 0; h2 < hits.get(h).length; h2++)
										hitz[h][h2] = hits.get(h)[h2];
								}
								attack.setHits(hitz);
							}
						}
						attacks.add(attack);
					}
				}
				// int health, int[] accuracy, int[] defence, Attack[] attacks,
				// int blockAnimation
				NpcCombatDef def = new NpcCombatDef(health, attacks.toArray(new Attack[attacks.size()]), blockAnim, deathAnim);
				for (int id : npcIds)
					list.put(id, def);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		combatDefs = list;
	}
}
