import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramBot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        String s = "";
        try{
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void sendMessage(Message m, String text){
        SendMessage send = new SendMessage();
        send.enableMarkdown(true);
        send.setChatId(m.getChatId().toString());
        send.setReplyToMessageId(m.getMessageId());
        send.setText(text);
        try{
            execute(send);
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
                    sendMessage(m, "Введите время");
                    break;
                case "/start":
                    sendMessage(m, "Добро пожаловать! Вас приветствует WeatherBot.");
                    getCommands(m);
                    break;
                case "/city":
                    changeCity(m);
                    break;
            }
        }
    }

    private void changeCity(Message m) {
    }

    private void getGreetings(Message m) {
    }

    private void changeTime(Message m) {

    }

    private void getCommands(Message m) {
        String commands = "Вам доступны команды:\n/time - время, в которое каждый день будет отправляться погода\n/city - установить город\n";
        sendMessage(m, commands);
    }
}
