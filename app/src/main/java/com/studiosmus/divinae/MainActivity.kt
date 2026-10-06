package com.studiosmus.divinae
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.*
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay

private val Ink=Color(0xFF2B190F);private val Red=Color(0xFF7D1F18);private val Gold=Color(0xFFC59A45)
private val Book=FontFamily(Font(R.font.im_fell_english));private val Modern=FontFamily(Font(R.font.eb_garamond))
class MainActivity:ComponentActivity(){override fun onCreate(b:Bundle?){super.onCreate(b);enableEdgeToEdge();setContent{MaterialTheme{ReaderV2()}}}}

@Composable private fun ReaderV2(){
 val context=LocalContext.current;val canto=remember{CantoRepository.load(context)}
 var page by rememberSaveable{mutableIntStateOf(0)};var selected by rememberSaveable{mutableStateOf<Int?>(null)}
 var drawer by rememberSaveable{mutableStateOf<String?>(null)};var tabs by rememberSaveable{mutableStateOf(false)}
 var fireOn by rememberSaveable{mutableStateOf(false)};var fireStatus by rememberSaveable{mutableStateOf("PRONTO")};var fireVolume by rememberSaveable{mutableFloatStateOf(.55f)}
 var musicOn by rememberSaveable{mutableStateOf(true)};var musicVolume by rememberSaveable{mutableFloatStateOf(.42f)};var musicStatus by rememberSaveable{mutableStateOf("PRONTO")}
 var musicPlayer by remember{mutableStateOf<MediaPlayer?>(null)};var activeMusic by remember{mutableStateOf("")}
 val fireId=remember{context.resources.getIdentifier("fireplace","raw",context.packageName)}
 var firePlayer by remember{mutableStateOf<MediaPlayer?>(null)}
 LaunchedEffect(fireId){if(fireId!=0&&firePlayer==null){firePlayer=runCatching{MediaPlayer.create(context,fireId)}.getOrNull();firePlayer?.isLooping=true;fireStatus=if(firePlayer==null)"ERRORE AUDIO" else "PRONTO"}}
 val lifecycle=LocalLifecycleOwner.current.lifecycle
 val currentMusic=remember(page,canto){val per=3; canto.terzine[(page*per).coerceAtMost(canto.terzine.lastIndex)].music}
 val musicId=remember(currentMusic){when(currentMusic){"o_frondens"->context.resources.getIdentifier("music_selva","raw",context.packageName);"dies_irae"->context.resources.getIdentifier("music_smarrimento","raw",context.packageName);else->0}}
 DisposableEffect(lifecycle){val o=LifecycleEventObserver{_,e->if(e==Lifecycle.Event.ON_STOP){runCatching{firePlayer?.pause()};fireOn=false;fireStatus=if(firePlayer==null)"AUDIO NON INSTALLATO" else "PRONTO"}};lifecycle.addObserver(o);onDispose{lifecycle.removeObserver(o)}}
 LaunchedEffect(musicId,musicOn){
  if(!musicOn||musicId==0){runCatching{musicPlayer?.pause()};musicStatus=if(musicId==0)"TRACCIA NON DISPONIBILE" else "PAUSA";return@LaunchedEffect}
  if(activeMusic==currentMusic&&musicPlayer!=null){runCatching{musicPlayer?.start()};musicStatus="IN RIPRODUZIONE";return@LaunchedEffect}
  val old=musicPlayer;val fresh=runCatching{MediaPlayer.create(context,musicId)}.getOrNull()
  if(fresh==null){musicStatus="ERRORE AUDIO";return@LaunchedEffect}
  fresh.isLooping=true;fresh.setVolume(0f,0f);fresh.start()
  repeat(12){step->val k=(step+1)/12f;fresh.setVolume(musicVolume*k,musicVolume*k);old?.setVolume(musicVolume*(1f-k),musicVolume*(1f-k));delay(55)}
  runCatching{old?.stop();old?.release()};musicPlayer=fresh;activeMusic=currentMusic;musicStatus="IN RIPRODUZIONE"
 }
 LaunchedEffect(musicVolume){musicPlayer?.setVolume(musicVolume,musicVolume)}
 BackHandler(enabled=selected!=null||drawer!=null||tabs){when{selected!=null->selected=null;drawer!=null->drawer=null;else->tabs=false}}
 BoxWithConstraints(Modifier.fillMaxSize().background(Color(0xFF160B07))){
  val perPage=if(maxHeight>=760.dp)3 else 2;val pageCount=((canto.terzine.size+perPage-1)/perPage).coerceAtLeast(1);val start=page*perPage;val end=(start+perPage).coerceAtMost(canto.terzine.size)
  if(page==0) Image(painterResource(R.drawable.manuscript_frame),null,Modifier.fillMaxSize(),contentScale=ContentScale.Crop)
  else {
   Box(Modifier.fillMaxSize().background(Color(0xFF2A120A)))
   Box(Modifier.fillMaxSize().padding(horizontal=14.dp,vertical=20.dp).background(Color(0xFFE7C98F)))
   Box(Modifier.fillMaxSize().padding(horizontal=22.dp,vertical=28.dp).border(2.dp,Gold).padding(5.dp).border(1.dp,Red.copy(.72f)))
  }
  val pageMod=if(page==0) Modifier.fillMaxWidth(.72f).fillMaxHeight(.68f).align(Alignment.TopEnd).statusBarsPadding().padding(top=46.dp,end=34.dp)
              else Modifier.fillMaxWidth(.78f).fillMaxHeight(.70f).align(Alignment.TopCenter).statusBarsPadding().padding(top=42.dp)
  AnimatedContent(targetState=page,modifier=pageMod,transitionSpec={
   val forward=targetState>initialState
   ContentTransform(
    targetContentEnter=slideInHorizontally(tween(320,easing=FastOutSlowInEasing)){if(forward)it/3 else -it/3}+fadeIn(tween(220)),
    initialContentExit=slideOutHorizontally(tween(320,easing=FastOutSlowInEasing)){if(forward)-it/3 else it/3}+fadeOut(tween(180))
   )
  },label="manuscriptPage"){shownPage->
  val shownStart=shownPage*perPage;val shownEnd=(shownStart+perPage).coerceAtMost(canto.terzine.size)
  Box(Modifier.fillMaxSize().padding(horizontal=10.dp,vertical=8.dp)){
   Column(Modifier.fillMaxSize(),horizontalAlignment=Alignment.CenterHorizontally){
    Text(canto.cantica.uppercase(),fontFamily=Book,fontSize=13.sp,letterSpacing=3.sp,color=Red);Text("CANTO "+roman(canto.numero),fontFamily=Book,fontWeight=FontWeight.Bold,fontSize=22.sp,color=Ink)
    Box(Modifier.padding(vertical=6.dp).width(84.dp).height(1.dp).background(Gold))
    Column(Modifier.weight(1f).fillMaxWidth()){(shownStart until shownEnd).forEach{i->val t=canto.terzine[i];Column(Modifier.fillMaxWidth().clickable{selected=i}.padding(vertical=6.dp)){Text((i*3+1).toString(),fontFamily=Book,fontSize=11.sp,color=Red);Text(t.versi,fontFamily=Book,fontSize=15.sp,lineHeight=20.sp,color=Ink)}}}
    Text("Tocca una terzina per comprenderla",fontFamily=Book,fontSize=11.sp,color=Ink.copy(.62f))
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Text("‹",Modifier.clickable(enabled=page>0){page--;selected=null}.padding(12.dp),fontSize=30.sp,color=if(page>0)Red else Ink.copy(.2f));Text((page+1).toString()+" / "+pageCount,fontFamily=Book,fontSize=13.sp,color=Ink);Text("›",Modifier.clickable(enabled=page<pageCount-1){page++;selected=null}.padding(12.dp),fontSize=30.sp,color=if(page<pageCount-1)Red else Ink.copy(.2f))}
   }
  }}
  Box(Modifier.align(Alignment.CenterEnd)){if(!tabs&&drawer==null)Text("‹",Modifier.background(Color(0xDD4A2417)).clickable{tabs=true}.padding(8.dp,18.dp),color=Gold,fontSize=20.sp);if(tabs&&drawer==null)Column{listOf("♫" to "Musica","🔥" to "Atmosfera","☰" to "Indice").forEach{p->Text(p.first,Modifier.padding(4.dp).background(Color(0xEE4A2417)).clickable{drawer=p.second;tabs=false}.padding(12.dp),fontSize=20.sp,color=Gold)}}}
  AnimatedVisibility(drawer!=null,Modifier.align(Alignment.CenterEnd)){Column(Modifier.width(230.dp).background(Color(0xFFF3E8CF)).padding(16.dp)){Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(drawer?:"",fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=19.sp,color=Red);Text("×",Modifier.clickable{drawer=null}.padding(8.dp),fontSize=24.sp,color=Ink)}
   when(drawer){"Atmosfera"->{Text("Camino",fontFamily=Modern,fontSize=16.sp,color=Ink);Text(if(fireId==0)"AUDIO NON INSTALLATO" else fireStatus,fontFamily=Modern,fontSize=11.sp,color=Ink.copy(.65f));Text(if(fireOn)"🔥 Spegni" else "🔥 Accendi",Modifier.fillMaxWidth().clickable{val p=firePlayer;if(p==null)fireStatus="AUDIO NON INSTALLATO" else if(fireOn){runCatching{p.pause()};fireOn=false;fireStatus="PRONTO"}else{val ok=runCatching{p.setVolume(fireVolume,fireVolume);p.start();p.isPlaying}.getOrDefault(false);fireOn=ok;fireStatus=if(ok)"IN RIPRODUZIONE" else "ERRORE RIPRODUZIONE"}}.padding(vertical=14.dp),fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=16.sp,color=Red);Slider(fireVolume,{fireVolume=it;firePlayer?.setVolume(it,it)},colors=SliderDefaults.colors(thumbColor=Red,activeTrackColor=Gold))}
   "Musica"->{Text("Colonna sonora narrativa",fontFamily=Modern,fontSize=16.sp,color=Ink);Text(currentMusic.replace("_"," ")+" · "+musicStatus,fontFamily=Modern,fontSize=11.sp,color=Ink.copy(.65f));Text(if(musicOn)"♫ Pausa" else "♫ Riproduci",Modifier.fillMaxWidth().clickable{musicOn=!musicOn}.padding(vertical=14.dp),fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=16.sp,color=Red);Slider(musicVolume,{musicVolume=it},colors=SliderDefaults.colors(thumbColor=Red,activeTrackColor=Gold))};else->Text("INFERNO\n• Canto I — "+canto.titolo+"\n\nPURGATORIO\nPARADISO",fontFamily=Modern,fontSize=14.sp,lineHeight=21.sp,color=Ink)}}}
  selected?.let{i->ExplanationV2(canto.terzine[i],i){selected=null}}
 }
}
@Composable private fun ExplanationV2(t:TerzinaV2,index:Int,onClose:()->Unit){val consume=remember{MutableInteractionSource()};Box(Modifier.fillMaxSize().background(Color(0xD921130D)).clickable{onClose()}.padding(20.dp),contentAlignment=Alignment.Center){Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFFF3E5C3)).clickable(consume,indication=null){}.padding(22.dp)){Text("INFERNO · CANTO I",fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=12.sp,color=Red);Text("VERSI "+(index*3+1)+"–"+(index*3+3),fontFamily=Modern,fontSize=11.sp,color=Ink.copy(.6f));Text(t.versi,fontFamily=Book,fontSize=19.sp,lineHeight=26.sp,color=Ink,modifier=Modifier.padding(top=16.dp));Text("In parole di oggi",fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=17.sp,color=Red,modifier=Modifier.padding(top=18.dp));Text(t.moderno,fontFamily=Modern,fontSize=16.sp,lineHeight=23.sp,color=Ink);Text("Che cosa sta dicendo Dante",fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=17.sp,color=Red,modifier=Modifier.padding(top=16.dp));Text(t.nota,fontFamily=Modern,fontSize=16.sp,lineHeight=23.sp,color=Ink);Text("Tocca fuori dalla scheda per tornare",fontFamily=Modern,fontSize=12.sp,textAlign=TextAlign.Center,color=Ink.copy(.55f),modifier=Modifier.fillMaxWidth().padding(top=20.dp))}}}
private fun roman(n:Int)=when(n){1->"I";2->"II";3->"III";else->n.toString()}
