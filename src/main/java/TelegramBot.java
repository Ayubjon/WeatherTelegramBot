import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TelegramBot extends TelegramLongPollingBot {

    private String city;
    private String date;
    private String chatId;
    private boolean isCity;
    private boolean isWaitingMessage;

    private final TimerTask timer_task = new TimerTask() {
        @Override
        public void run() {
            System.out.println("timer run");
            Timer timer_everyDay = new Timer();
            Date date = new Date();
            long hours_now = date.getHours();
            long minutes_now = date.getMinutes();
            long seconds = date.getSeconds();
            hours_now = hours_now*3600000 + minutes_now*60000 + seconds*1000;
            Parser parser = new Parser(city);
            try {
                sendMessage(parser.getTemperature());
            } catch (IOException e) {
                sendMessage("Непредвиденная ошибка! Советуем поменять город");
            }
            System.out.println("timer end");
            timer_everyDay.schedule(get_weather_task, hours_now, TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
        }
    };

    private final TimerTask get_weather_task = new TimerTask() {
        @Override
        public void run() {
            Parser parser = new Parser(city);
            try {
                sendMessage(parser.getTemperature());
            } catch (IOException e) {
                sendMessage("Непредвиденная ошибка! Советуем поменять город");
            }
        }
    };

    private void sendMessage(Message m, String text){
        try{
            execute(new SendMessage(m.getChatId().toString(), text));
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void sendMessage(String text){
        try{
            execute(new SendMessage(chatId, text));
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        return "WeatherYasliBot";
    }

    @Override
    public String getBotToken() {
        return "1691577445:AAEQ_jyD680JigZfdHSMxD3aF90dSZ9KamE";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message m = update.getMessage();
        if(m != null && m.hasText()){
            switch (m.getText()){
                case "/help":
                    getCommands(m);
                    break;
                case "/time":
                    isWaitingMessage = true;
                    isCity = false;
                    sendMessage(m, "Введите время\nНапример: 18:00");
                    break;
                case "/start":
                    sendMessage(m, "Добро пожаловать! Вас приветствует WeatherBot.");
                    getCommands(m);
                    break;
                case "/city":
                    isWaitingMessage = true;
                    isCity = true;
                    sendMessage(m, "Введите город\nНапример: Санкт-Петербург");
                    break;
                default:
                    if (!isWaitingMessage){
                        sendMessage("");
                    }
                    if(isCity) {
                        Parser parser = new Parser(m.getText());
                        try {
                            parser.getTemperature();
                            city = m.getText();
                            sendMessage(m, "Город успешно сохранен");
                        } catch (IOException e) {
                            sendMessage(m, "Такого города нет!\nПопробуйте снова");
                            sendMessage(m, "Введите город\nНапример: Санкт-Петербург");
                        }

                    } else {
                        try{
                            Date date = new Date();
                            long hours_now = date.getHours();
                            long minutes_now = date.getMinutes();
                            long seconds = date.getSeconds();
                            long hours = Long.parseLong(m.getText().substring(0, 2));
                            long minutes = Long.parseLong(m.getText().substring(3, 5));
                            hours = (hours_now >= hours ? hours_now - hours : hours - hours_now)*3600000;
                            minutes = (minutes_now >= minutes ? minutes_now - minutes : minutes - minutes_now)*60000 + hours - seconds*1000;
                            chatId = m.getChatId().toString();
                            if(city != null && !city.isEmpty()){
                                sendMessage(m, "Время успешно сохранено");
                                System.out.println(city);
                                sendMessage(m, city);
                                Timer timer_near = new Timer();
                                System.out.println("timer starts");
                                timer_near.schedule(timer_task, minutes);
                            } else {
                                sendMessage(m, "Введите город\nНапример: Санкт-Петербург");
                            }
                        } catch (NumberFormatException e){
                            sendMessage(m, "Неправильный формат времени!\nПопробуйте снова");
                            sendMessage(m, "Введите время\nНапример: 18:00");
                        }
                    }
            }
        }
    }

    private void getCommands(Message m) {
        String commands = "Вам доступны команды:\n/time - время, в которое каждый день будет отправляться погода\n/city - установить город\n";
        sendMessage(m, commands);
    }
}
