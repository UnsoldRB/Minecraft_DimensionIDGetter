package com.unsoldriceball.dimensionidgetter;

import it.unimi.dsi.fastutil.ints.IntSortedSet;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;




@Mod(modid = Main.MOD_ID, acceptableRemoteVersions = "*")
public class Main
{
    public static final String MOD_ID = "dimensionidgetter";
    public static final String MOD_NAME = "dimensionIDGetter";




    //ModがInitializeを呼び出す前に発生するイベント。
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        //これでこのクラス内でForgeのイベントが動作するようになるらしい。
        MinecraftForge.EVENT_BUS.register(this);
    }



    //プレイヤーがワールドにログインしたときのイベント。
    @SubscribeEvent
    public void enterWorld(PlayerEvent.PlayerLoggedInEvent event)
    {
        final Map<DimensionType, IntSortedSet> LDIMENSIONS = DimensionManager.getRegisteredDimensions();    //登録されたDimension一覧を取得
        Set<Integer> dimensionIDs = new TreeSet<>();                                                        //登録されたDimensionIDの一覧が入る


        //全てのDimensionIDをSetに格納していく。
        for (IntSortedSet _idSet : LDIMENSIONS.values())
        {
            dimensionIDs.addAll(_idSet);
        }

        final String LINDENTION = System.getProperty("line.separator");     //改行コード
        StringBuilder dim_comma = new StringBuilder();                      //コンマで区切られたDimensionIDを組み立てる。
        StringBuilder dim_indention = new StringBuilder();                  //改行で区切られたDimensionIDを組み立てる。

        //全てのDimensionIDをSetに格納していく。
        for (int _i : dimensionIDs)
        {
            String _dim_id = String.valueOf(_i);        //ObjectからStringに変換されたDimensionIDが入る。

            dim_comma.append(_dim_id).append(", ");
            dim_indention.append(_dim_id).append(LINDENTION);
        }
        //それぞれ最後のコンマと改行コードを取り除いて結合し、{}を取り除く。
        final String LRESULT_COMMA = dim_comma.substring(0, dim_comma.length() - 2);
        final String LRESULT_INDENTION = dim_indention.substring(0, dim_indention.length() - 2);
        String result = LRESULT_COMMA + LINDENTION + LINDENTION + LRESULT_INDENTION;

        final String LGAMEDIR = Loader.instance().getConfigDir().getParentFile().getAbsolutePath();     //ファイル生成先のディレクトリ
        final String LFILEPATH = LGAMEDIR + File.separator + MOD_NAME + ".txt";
        File file = new File(LFILEPATH);                                                                //ファイル作成
        FileWriter writer;

        try {
            writer = new FileWriter(file);
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
