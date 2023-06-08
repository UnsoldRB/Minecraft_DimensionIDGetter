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
public class Main {
    public static final String MOD_ID = "dimensionidgetter";
    public static final String MOD_NAME = "dimensionIDGetter";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){ //ModがInitializeを呼び出す前に発生するイベント。
        MinecraftForge.EVENT_BUS.register(this); //これでこのクラス内でForgeのイベントが動作するようになるらしい。
    }



    @SubscribeEvent
    public void enterWorld(PlayerEvent.PlayerLoggedInEvent event) { //プレイヤーがワールドにログインしたときのイベント。
        final Map<DimensionType, IntSortedSet> DIMENSIONS = DimensionManager.getRegisteredDimensions(); //登録されたDimension一覧を取得
        Set<Integer> dimensionIDs = new TreeSet<>();

        for (IntSortedSet idSet : DIMENSIONS.values()) { //全てのDimensionIDをSetに格納していく。
            dimensionIDs.addAll(idSet);
        }

        String dim_id = ""; //1ループごとにObjectからStringに変換されたDimensionIDが入る。
        StringBuilder dim_comma = new StringBuilder(); //コンマで区切られたDimensionIDを組み立てる。
        StringBuilder dim_indention = new StringBuilder(); //改行で区切られたDimensionIDを組み立てる。
        final String INDENTION = System.getProperty("line.separator"); //改行コード

        for (int i : dimensionIDs) { //全てのDimensionIDをSetに格納していく。
            dim_id = String.valueOf(i);

            dim_comma.append(dim_id).append(", ");
            dim_indention.append(dim_id).append(INDENTION);
        }
        //それぞれ最後のコンマと改行コードを取り除いて結合し、{}を取り除く。
        final String RESULT_COMMA = dim_comma.substring(0, dim_comma.length() - 2);
        final String RESULT_INDENTION = dim_indention.substring(0, dim_indention.length() - 2);
        String result = RESULT_COMMA + INDENTION + INDENTION + RESULT_INDENTION;

        final String GAMEDIR = Loader.instance().getConfigDir().getParentFile().getAbsolutePath(); //ファイル生成先のディレクトリ


        final String FILEPATH = GAMEDIR + File.separator + MOD_NAME + ".txt";
        File file = new File(FILEPATH); //ファイル作成

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
