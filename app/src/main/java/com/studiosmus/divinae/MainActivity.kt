package com.studiosmus.divinae
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.BackHandler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
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

private val Ink=Color(0xFF2B190F);private val Red=Color(0xFF7D1F18);private val Gold=Color(0xFFC59A45)
private val Book=FontFamily(Font(R.font.im_fell_english));private val Modern=FontFamily(Font(R.font.eb_garamond))
class MainActivity:ComponentActivity(){override fun onCreate(b:Bundle?){super.onCreate(b);enableEdgeToEdge();setContent{MaterialTheme{ReaderV2()}}}}

@Composable private fun ReaderV2(){
 val context=LocalContext.current;val canto=remember{CantoRepository.load(context)}
 var page by rememberSaveable{mutableIntStateOf(0)};var selected by rememberSaveable{mutableStateOf<Int?>(null)}
 var drawer by rememberSaveable{mutableStateOf<String?>(null)};var tabs by rememberSaveable{mutableStateOf(false)}
 var fireOn by rememberSaveable{mutableStateOf(false)};var fireStatus by rememberSaveable{mutableStateOf("PRONTO")};var fireVolume by rememberSaveable{mutableFloatStateOf(.55f)}
 val fireId=remember{context.resources.getIdentifier("fireplace","raw",context.packageName)}
 val firePlayer=remember(fireId){if(fireId!=0)MediaPlayer.create(context,fireId) else null}
 val lifecycle=LocalLifecycleOwner.current.lifecycle
 DisposableEffect(firePlayer,lifecycle){firePlayer?.isLooping=true;val o=LifecycleEventObserver{_,e->if(e==Lifecycle.Event.ON_STOP){runCatching{firePlayer?.pause()};fireOn=false;fireStatus=if(firePlayer==null)"AUDIO NON INSTALLATO" else "PRONTO"}};lifecycle.addObserver(o);onDispose{lifecycle.removeObserver(o);runCatching{firePlayer?.release()}}}
 BackHandler(enabled=selected!=null||drawer!=null||tabs){when{selected!=null->selected=null;drawer!=null->drawer=null;else->tabs=false}}
 BoxWithConstraints(Modifier.fillMaxSize().background(Color(0xFF160B07))){
  val perPage=if(maxHeight>=760.dp)3 else 2;val pageCount=((canto.terzine.size+perPage-1)/perPage).coerceAtLeast(1);val start=page*perPage;val end=(start+perPage).coerceAtMost(canto.terzine.size)
  Image(painterResource(R.drawable.manuscript_frame),null,Modifier.fillMaxSize(),contentScale=ContentScale.Crop)
  Box(Modifier.fillMaxWidth(.84f).fillMaxHeight(.76f).align(Alignment.TopCenter).statusBarsPadding().padding(top=18.dp).clip(RoundedCornerShape(5.dp)).background(Color(0xFFF1DEB5)).padding(horizontal=22.dp,vertical=18.dp)){
   Column(Modifier.fillMaxSize(),horizontalAlignment=Alignment.CenterHorizontally){
    Text(canto.cantica.uppercase(),fontFamily=Book,fontSize=13.sp,letterSpacing=3.sp,color=Red);Text("CANTO "+roman(canto.numero),fontFamily=Book,fontWeight=FontWeight.Bold,fontSize=22.sp,color=Ink)
    Box(Modifier.padding(vertical=6.dp).width(84.dp).height(1.dp).background(Gold))
    Column(Modifier.weight(1f).fillMaxWidth()){(start until end).forEach{i->val t=canto.terzine[i];Column(Modifier.fillMaxWidth().clickable{selected=i}.padding(vertical=6.dp)){Text((i*3+1).toString(),fontFamily=Book,fontSize=11.sp,color=Red);Text(t.versi,fontFamily=Book,fontSize=16.sp,lineHeight=21.sp,color=Ink)}}}
    Text("Tocca una terzina per comprenderla",fontFamily=Book,fontSize=11.sp,color=Ink.copy(.62f))
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){Text("‹",Modifier.clickable(enabled=page>0){page--;selected=null}.padding(12.dp),fontSize=30.sp,color=if(page>0)Red else Ink.copy(.2f));Text((page+1).toString()+" / "+pageCount,fontFamily=Book,fontSize=13.sp,color=Ink);Text("›",Modifier.clickable(enabled=page<pageCount-1){page++;selected=null}.padding(12.dp),fontSize=30.sp,color=if(page<pageCount-1)Red else Ink.copy(.2f))}
   }
  }
  Box(Modifier.align(Alignment.CenterEnd)){if(!tabs&&drawer==null)Text("‹",Modifier.background(Color(0xDD4A2417)).clickable{tabs=true}.padding(8.dp,18.dp),color=Gold,fontSize=20.sp);if(tabs&&drawer==null)Column{listOf("♫" to "Musica","🔥" to "Atmosfera","☰" to "Indice").forEach{p->Text(p.first,Modifier.padding(4.dp).background(Color(0xEE4A2417)).clickable{drawer=p.second;tabs=false}.padding(12.dp),fontSize=20.sp,color=Gold)}}}
  AnimatedVisibility(drawer!=null,Modifier.align(Alignment.CenterEnd)){Column(Modifier.width(230.dp).background(Color(0xFFF3E8CF)).padding(16.dp)){Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween){Text(drawer?:"",fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=19.sp,color=Red);Text("×",Modifier.clickable{drawer=null}.padding(8.dp),fontSize=24.sp,color=Ink)}
   when(drawer){"Atmosfera"->{Text("Camino",fontFamily=Modern,fontSize=16.sp,color=Ink);Text(if(fireId==0)"AUDIO NON INSTALLATO" else fireStatus,fontFamily=Modern,fontSize=11.sp,color=Ink.copy(.65f));Text(if(fireOn)"🔥 Spegni" else "🔥 Accendi",Modifier.fillMaxWidth().clickable{val p=firePlayer;if(p==null)fireStatus="AUDIO NON INSTALLATO" else if(fireOn){runCatching{p.pause()};fireOn=false;fireStatus="PRONTO"}else{val ok=runCatching{p.setVolume(fireVolume,fireVolume);p.start();p.isPlaying}.getOrDefault(false);fireOn=ok;fireStatus=if(ok)"IN RIPRODUZIONE" else "ERRORE RIPRODUZIONE"}}.padding(vertical=14.dp),fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=16.sp,color=Red);Slider(fireVolume,{fireVolume=it;firePlayer?.setVolume(it,it)},colors=SliderDefaults.colors(thumbColor=Red,activeTrackColor=Gold))}
   "Musica"->Text("Musica: migrazione offline in corso",fontFamily=Modern,fontSize=14.sp,color=Ink);else->Text("INFERNO\n• Canto I — "+canto.titolo+"\n\nPURGATORIO\nPARADISO",fontFamily=Modern,fontSize=14.sp,lineHeight=21.sp,color=Ink)}}}
  selected?.let{i->ExplanationV2(canto.terzine[i],i){selected=null}}
 }
}
@Composable private fun ExplanationV2(t:TerzinaV2,index:Int,onClose:()->Unit){val consume=remember{MutableInteractionSource()};Box(Modifier.fillMaxSize().background(Color(0xD921130D)).clickable{onClose()}.padding(20.dp),contentAlignment=Alignment.Center){Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFFF3E5C3)).clickable(consume,indication=null){}.padding(22.dp)){Text("INFERNO · CANTO I",fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=12.sp,color=Red);Text("VERSI "+(index*3+1)+"–"+(index*3+3),fontFamily=Modern,fontSize=11.sp,color=Ink.copy(.6f));Text(t.versi,fontFamily=Book,fontSize=19.sp,lineHeight=26.sp,color=Ink,modifier=Modifier.padding(top=16.dp));Text("In parole di oggi",fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=17.sp,color=Red,modifier=Modifier.padding(top=18.dp));Text(t.moderno,fontFamily=Modern,fontSize=16.sp,lineHeight=23.sp,color=Ink);Text("Che cosa sta dicendo Dante",fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=17.sp,color=Red,modifier=Modifier.padding(top=16.dp));Text(t.nota,fontFamily=Modern,fontSize=16.sp,lineHeight=23.sp,color=Ink);Text("Tocca fuori dalla scheda per tornare",fontFamily=Modern,fontSize=12.sp,textAlign=TextAlign.Center,color=Ink.copy(.55f),modifier=Modifier.fillMaxWidth().padding(top=20.dp))}}}
private fun roman(n:Int)=when(n){1->"I";2->"II";3->"III";else->n.toString()}
