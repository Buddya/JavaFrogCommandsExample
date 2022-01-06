import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //...
        Frog frog = new Frog();
        final Scanner scanner = new Scanner(System.in);
        List<FrogCommand> commands = new ArrayList<>();
        int curCommand = -1;
        while (true) {
            //считываем ввод и конструируем комманду, если
            //пользователь не хотел выйти
            System.out.println("Введите команду для лягушки");
            String newCmd = scanner.nextLine();
            if (newCmd.equals("0")) {
                System.out.println("Завершение программы");
                break;
            }

            switch (newCmd) {
                case "<<":
                    if (curCommand < 0) {
                        System.out.println("Нечего отменять!");
                    } else {
                        commands.get(curCommand).undo();
                        curCommand--;
                    }
                    break;
                case ">>":
                    if (curCommand == commands.size() - 1) {
                        System.out.println("Нет отмененных действий для повторения!");
                    } else {
                        curCommand++;
                        commands.get(curCommand).doit();
                    }
                    break;
                case "!!":
                    if (curCommand < 0) {
                        System.out.println("Нет действий для повторения");
                    } else {
                        deleteCanceledCmd(commands, curCommand);
                        FrogCommand frogCommand = commands.get(curCommand);
                        commands.add(frogCommand);
                        frogCommand.doit();
                    }
                    break;
                default:  //пользователь ввёл новое движение для лягушки
                    deleteCanceledCmd(commands, curCommand);
                    FrogCommand cmd = getCommand(frog, newCmd);
                    curCommand++;
                    commands.add(cmd);
                    cmd.doit();
                    break;
            }

            //рисуем поле после команды
            drawField(frog);
        }
    }

    private static FrogCommand getCommand(Frog frog, String newCmd) {
        if (newCmd.length() < 2) {
            throw new IllegalArgumentException("Неизвестная команда!");
        }
        String direction = newCmd.substring(0, 1);
        int steps = Integer.parseInt(newCmd.substring(1));
        switch (direction) {
            case "-" -> {
                return FrogCommands.jumpLeftCommand(frog, steps);
            }
            case "+" -> {
                return FrogCommands.jumpRightCommand(frog, steps);
            }
            default -> {
                throw new IllegalArgumentException("Неизвестная команда!");
            }
        }
    }

    private static void deleteCanceledCmd(List<FrogCommand> commands, int curCommand) {
        if (curCommand != commands.size() - 1) {
            //удаляем все команды которые были отменены
            commands.subList(curCommand + 1, commands.size()).clear();
        }
    }

    private static void drawField(Frog frog) {
        for (int i = Frog.MIN_POSITION; i < Frog.MAX_POSITION; i++) {
            if (i == frog.position) {
                System.out.print("@");
            } else {
                System.out.print("_");
            }
        }
        System.out.println();
    }
}
