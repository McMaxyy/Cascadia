package config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class Storage {
	private static Storage instance = null;
	public Skin skin;
	public TextButton.TextButtonStyle buttonStyle;
	public LabelStyle labelStyle;
	public TextFieldStyle textStyle;
	public BitmapFont font, fontSm;
	public static AssetManager assetManager = new AssetManager();
	private static boolean newLoad = true;	
	
	public static synchronized Storage getInstance()  {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }
	
	public Storage() {
		skin = new Skin(Gdx.files.internal("buttons/newskin/newskin.json"));	
		if(newLoad) {
			newLoad = false;
			loadAssets();
		}		
	}	
	
	public static void loadAssets() {
		TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter(); 
		textureParameter.genMipMaps = true; 
		
		assetManager.load("A.png", Texture.class, textureParameter);
		assetManager.load("B.png", Texture.class, textureParameter);
		assetManager.load("C.png", Texture.class, textureParameter);
		assetManager.load("D.png", Texture.class, textureParameter);
	}
	
	public void createFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Kushin.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 25;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        font = generator.generateFont(parameter);
        parameter.size = 20;
        fontSm = generator.generateFont(parameter);
        parameter.color = Color.BLACK;
        
        Texture borderTextureUp = new Texture(Gdx.files.internal("buttons/newskin/newskin_data/textbutton.9.png"));
        Texture borderTextureDown = new Texture(Gdx.files.internal("buttons/newskin/newskin_data/textbutton-down.9.png"));
        Texture homeBorderTextureUp = new Texture(Gdx.files.internal("buttons/newskin/newskin_data/homeButton.9.png"));
        Texture homeBorderTextureDown = new Texture(Gdx.files.internal("buttons/newskin/newskin_data/homeButton-down.9.png"));
        NinePatch borderPatchUp = new NinePatch(borderTextureUp, 1, 1, 1, 1);
        NinePatch borderPatchDown = new NinePatch(borderTextureDown, 1, 1, 1, 1);
        NinePatch homeBorderPatchUp = new NinePatch(homeBorderTextureUp, 1, 1, 1, 1);
        NinePatch homeBorderPatchDown = new NinePatch(homeBorderTextureDown, 1, 1, 1, 1);
        
        buttonStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        buttonStyle.up = new NinePatchDrawable(borderPatchUp);
        buttonStyle.down = new NinePatchDrawable(borderPatchDown);
        buttonStyle.font = font;
        
        labelStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        labelStyle.font = font;  
        
        textStyle = new TextField.TextFieldStyle(skin.get(TextField.TextFieldStyle.class));
        textStyle.font = fontSm;  
	}
}



