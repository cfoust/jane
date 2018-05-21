package com.sqweebloid.jane;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.someclient.api.Client;
import org.someclient.api.Point;
import org.someclient.client.ui.ClientUI;
import org.someclient.client.ui.DrawManager;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sqweebloid.jane.Utils;

@Singleton
public class Push {
	private static final Logger logger = LoggerFactory.getLogger(Push.class);

    @Inject
    private Client client;

	@Inject
	private ClientUI clientUi;

	@Inject
	private DrawManager drawManager;

    private final OkHttpClient httpClient = new OkHttpClient();

	public static final File SCREENSHOT_DIR = new File(
            System.getProperty("user.home"), ".jane");
	public static final File SCREENSHOT = new File(
            SCREENSHOT_DIR, "screencap.png");

    public static final String TOKEN = "***REMOVED***";
    public static final String USER = "***REMOVED***";
    public static final String FILE_NAME = "push_shot";

    private boolean shouldSend = false;

    public void setShouldSend(boolean shouldSend) {
        this.shouldSend = shouldSend;
    }

	private void takeScreenshot() {
        if (SCREENSHOT.exists()) {
            SCREENSHOT.delete();
        }

		drawManager.requestNextFrameListener(image -> {
			BufferedImage screenshot = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);

			Graphics graphics = screenshot.getGraphics();
			graphics.drawImage(image, 0, 0, null);

			SCREENSHOT_DIR.mkdirs();

            try {
                ImageIO.write(screenshot, "PNG", SCREENSHOT);
            } catch (IOException ex) {
                logger.error("error writing screenshot");
            }
		});
	}

    private RequestBody makeScreenshotBody(String message, boolean silent) {
        takeScreenshot();

        Utils.sleep(500);

        MultipartBody.Builder builder = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("attachment", SCREENSHOT.getName(),
                    RequestBody.create(MediaType.parse("image/png"), SCREENSHOT))
            .addFormDataPart("token", TOKEN)
            .addFormDataPart("user", USER)
            .addFormDataPart("message", message);

        if (silent) {
            builder.addFormDataPart("priority", "" + -1);
        }

        return builder.build();
    }

    private RequestBody makeFormBody(String message, boolean silent) {
        FormBody.Builder form = new FormBody.Builder()
            .add("token", TOKEN)
            .add("user", USER)
            .add("message", message);

        if (silent) {
            form.add("priority", "" + -1);
        }

        return form.build();
    }

    public void sendShot(String message) {
        sendMessage(message, false, true);
    }

    public void sendMessage(String message) {
        sendMessage(message, false, false);
    }

    public void sendSilent(String message) {
        sendMessage(message, true, false);
    }

    public void sendMessage(String message, boolean silent, boolean screenshot) {
        if (!shouldSend) return;

        System.out.printf("Sending push: %s\n", message);

        Request request = new Request.Builder()
            .url("https://api.pushover.net/1/messages.json")
            .post(screenshot ?
                    makeScreenshotBody(message, silent) :
                    makeFormBody(message, silent))
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            logger.info(response.body().string());
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}
