package view;

import javax.swing.*;
import java.awt.*;
/**Justin
 * view.Frame
 * GUI pop ups for if user types in help (NOTHING ELSE)
 * */
//justin
public class Frame {
        public void helpMenu() {
                JFrame jFrame = new JFrame("Help Menu");
                JTextArea jTextArea = new JTextArea("\t\t\tHelp Menu\n\ttype help to view this help menu\n" +
                        "\ttype map to view the map\n" +
                        "\ttype quit to end the game\n" +
                        "\ttype stat stats or st to view player stats\n" +
                        "\ttype inventory or inv to view player inventory (MAX 15 items)\n" +
                        "\n\tNAVIGATION-type go + direction you want to go (north south east west n s e w) not case sensitive\n" +
                        "\tIN ROOM-type explore or ex to gather information about the room (tells you if there's items or puzzles in room)\n" +
                        "\tIN ROOM-type inspect or ins to view items and puzzles in the room(shows you the items and puzzles in room)\n" +
                        "\n\tITEMS-type take + itemName or itemNameShortcut to pick up an item\n" +
                        "\tTo take multiple ITEMS type take + itemName or itemNameShortcut + number you want to pick up\n" +
                        "\tITEMS-type drop + itemName or itemNameShortcut to drop a item\n" +
                        "\tTo drop multiple ITEMS type take + itemName or itemNameShortcut + number you want to drop\n" +
                        "\tTo use an ITEM type use + itemName or itemNameShortCut to use a CONSUMABLE ITEM\n" +
                        "\tSome rooms are locked and require an item to be in your inventory for room to be unlocked\n" +
                        "\n\tCOMBAT-When entering room with monster you will have 2 options\n" +
                        "\t\btype yes to enter combat with the monster\t\btyping anything else will avoid monster\n" +
                        "\tIf yes will show you 4 options (attack with weapon,use item,flee,equip weapon)\n" +
                        "\tattack with weapon causes fight cycle causing you and monster damage\n" +
                        "\tuse item lets you use a non weapon item during combat (mostly for healing)\n" +
                        "\tflee lets you escape the combat\n" +
                        "\tequip weapon lets you equip weapon for more damage (attack with weapon with no equipped is fists)\n" +
                        "\tYou can type help during combat for info in console(Fleeing and ignoring keep monster in room)\n" +
                        "\n\tPUZZLE-When in room with puzzle after inspect it will say 'You uncover a hidden mechanism... It's a puzzle!'\n" +
                        "\tYou can type try to attempt the puzzle which will display the puzzle and how to complete it\n" +
                        "\tYou will be then giving multiple options \bsolve \bhint \bleave \buse item \binventory\n" +
                        "\tSOLVE-lets you type in answer (or item) to the puzzle\n" +
                        "\tHINT-After 3 failed attempts you are allowed to use hint which gives you a hint\n" +
                        "\tLEAVE-Leaves the puzzle and goes back to navigation\n" +
                        "\tUSE ITEM-Some puzzles require you to use an item to solve it so you type in full itemName-> itemName (this part to)\n" +
                        "\tINVENTORY-Checks player's inventory\n");

                jTextArea.setEditable(false);
                jTextArea.setPreferredSize(new Dimension(150, 150));
                jTextArea.setFont(new Font("Georgia", Font.PLAIN, 15));
                jTextArea.setForeground(new Color(196, 70, 58));


                jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jFrame.setSize(900, 700);
                jFrame.add(jTextArea);
                jFrame.setLocationRelativeTo(null);
                //jFrame.setResizable(false);
                jFrame.setAlwaysOnTop(true);
                jFrame.setVisible(true);
        }

        public void map(){//justin
                JFrame frame = new JFrame("Map");
                //ImageIcon imageIcon = new ImageIcon("HospitalMap.png");
                ImageIcon imageIcon = new ImageIcon("FinalGame_Project/HospitalMap.png");
                JLabel imageLabel = new JLabel(imageIcon);
                Image image = imageIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
                frame.add(imageLabel);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setAlwaysOnTop(true);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
        }
}

