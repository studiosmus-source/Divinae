package com.studiosmus.divinae

import android.os.Bundle
import android.media.MediaPlayer
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext

data class Terzina(val versi:String,val moderno:String,val nota:String,val contesto:String="",val simboli:String="",val lessico:String="")
private val cantoI=listOf(
 Terzina("""Nel mezzo del cammin di nostra vita
mi ritrovai per una selva oscura,
ché la diritta via era smarrita.""","A metà della vita mi ritrovo in un luogo oscuro: ho perso la strada giusta.","La selva è insieme luogo reale del racconto e immagine dello smarrimento di Dante.","Siamo all'inizio del viaggio. Dante narratore guarda indietro a una crisi profonda, collocata simbolicamente a metà della vita.","La selva oscura rappresenta perdita dell'orientamento morale e spirituale; la diritta via è il cammino verso il bene.","cammin: percorso della vita · diritta via: strada giusta, morale e spirituale"),
 Terzina("""Ahi quanto a dir qual era è cosa dura
esta selva selvaggia e aspra e forte
che nel pensier rinova la paura!""","È difficile perfino descrivere quanto quel bosco fosse terribile: ricordarlo fa tornare la paura.","Dante ci fa capire che quell'esperienza continua a scuoterlo anche mentre la racconta.","Il narratore interrompe il racconto per comunicarci direttamente la difficoltà di ricordare la selva.","La paura non appartiene soltanto al Dante-personaggio: sopravvive nel Dante che scrive, rendendo il ricordo ancora presente.","esta: questa · aspra e forte: difficile da attraversare e opprimente"),
 Terzina("""Tant’è amara che poco è più morte;
ma per trattar del ben ch’i’ vi trovai,
dirò de l’altre cose ch’i’ v’ho scorte.""","Fu un'esperienza quasi peggiore della morte; ma racconterò il bene che vi trovai e ciò che vidi.","La promessa è già qui: dentro lo smarrimento Dante troverà qualcosa capace di salvarlo.","Dante anticipa che dalla terribile esperienza nascerà anche un bene. È una promessa narrativa al lettore.","Il viaggio sarà discesa nel male ma anche conoscenza e possibilità di salvezza.","trattar: raccontare · scorte: viste, conosciute"),
 Terzina("""Io non so ben ridir com’i’ v’intrai,
tant’era pien di sonno a quel punto
che la verace via abbandonai.""","Non so spiegare come ci entrai: ero come addormentato quando abbandonai la via vera.","Il sonno indica inconsapevolezza: Dante si accorge dello smarrimento quando è già dentro la selva.","Dante non descrive un ingresso preciso nella selva: lo smarrimento è avvenuto gradualmente, quasi senza accorgersene.","Il sonno è immagine dell'ottundimento della coscienza; la verace via è la verità che è stata abbandonata.","ridir: raccontare con precisione · verace: vera, autentica")
)

private val Book = FontFamily(
    Font(R.font.im_fell_english, FontWeight.Normal)
)
private val Modern = FontFamily(Font(R.font.eb_garamond, FontWeight.Normal))
private val Ink=Color(0xFF2B190F)
private val Red=Color(0xFF7D1F18)
private val Gold=Color(0xFFC59A45)

class MainActivity:ComponentActivity(){
 override fun onCreate(savedInstanceState:Bundle?){
  enableEdgeToEdge(SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),SystemBarStyle.dark(android.graphics.Color.TRANSPARENT))
  super.onCreate(savedInstanceState)
  setContent{MaterialTheme{Divinae()}}
 }
}

@Composable fun Divinae(){
 var page by rememberSaveable{mutableIntStateOf(0)}
 var selected by rememberSaveable{mutableStateOf<Int?>(null)}
 var drawer by rememberSaveable{mutableStateOf<String?>(null)}
 var tabsVisible by rememberSaveable{mutableStateOf(false)}
 val context=LocalContext.current
 var musicOn by rememberSaveable{mutableStateOf(false)}
 var fireOn by rememberSaveable{mutableStateOf(false)}
 var musicVolume by rememberSaveable{mutableFloatStateOf(.55f)}
 var fireVolume by rememberSaveable{mutableFloatStateOf(.45f)}
 val musicPlayer=remember{MediaPlayer()}
 val firePlayer=remember{MediaPlayer.create(context,R.raw.fireplace)}
 var fireReady by remember{mutableStateOf(firePlayer != null)}
 DisposableEffect(Unit){
  musicPlayer.setOnPreparedListener{it.isLooping=true;it.setVolume(musicVolume,musicVolume);if(musicOn)it.start()}

  fun prepare(p:MediaPlayer,url:String){runCatching{p.setDataSource(context,Uri.parse(url));p.prepareAsync()}}
  prepare(musicPlayer,DivinaeReaderEngine.darkWood.sourceUrl)
  firePlayer?.isLooping=true
  firePlayer?.setVolume(fireVolume,fireVolume)
  onDispose{runCatching{musicPlayer.release()};runCatching{firePlayer?.release()}}
 }
 LaunchedEffect(musicOn,musicVolume){musicPlayer.setVolume(musicVolume,musicVolume);if(musicOn){runCatching{musicPlayer.start()}}else{runCatching{musicPlayer.pause()}}}
 LaunchedEffect(fireOn,fireVolume,fireReady){firePlayer?.setVolume(fireVolume,fireVolume);if(fireOn){runCatching{firePlayer?.start()}}else{runCatching{firePlayer?.pause()}}}
 Box(Modifier.fillMaxSize().background(Color(0xFF100806))){
  androidx.compose.foundation.Image(
   painterResource(R.drawable.manuscript_frame),null,
   Modifier.fillMaxSize(),contentScale=ContentScale.FillBounds
  )
  // Il testo resta nativo: l'illustrazione è solo la materia fisica del manoscritto.
  Column(
   Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
    .padding(top=DivinaeReaderEngine.cantoOne.safeZone.top,bottom=DivinaeReaderEngine.cantoOne.safeZone.bottom),
   horizontalAlignment=Alignment.CenterHorizontally
  ){
   Column(Modifier.fillMaxWidth().padding(start=DivinaeReaderEngine.cantoOne.safeZone.start,end=DivinaeReaderEngine.cantoOne.safeZone.end),horizontalAlignment=Alignment.CenterHorizontally){
    Text("INFERNO",fontFamily=Book,fontWeight=FontWeight.Bold,fontSize=13.sp,letterSpacing=3.sp,color=Red)
    Text("CANTO I",fontFamily=Book,fontWeight=FontWeight.Bold,fontSize=22.sp,color=Ink)
    Box(Modifier.padding(vertical=7.dp).width(84.dp).height(1.dp).background(Gold.copy(.7f)))
   }
   Column(Modifier.weight(1f).fillMaxWidth().padding(start=DivinaeReaderEngine.cantoOne.safeZone.bodyStart,end=DivinaeReaderEngine.cantoOne.safeZone.bodyEnd).verticalScroll(rememberScrollState())){
    val range=if(page==0) 0..2 else 3..3
    range.forEach{ i ->
     val t=cantoI[i]
     val active=selected==i
     Column(
      Modifier.fillMaxWidth().padding(vertical=3.dp)
       .clip(RoundedCornerShape(4.dp))
       .background(if(active) Gold.copy(alpha=.19f) else Color.Transparent)
       .clickable{selected=i}.padding(horizontal=4.dp,vertical=3.dp)
     ){
      Text("${i*3+1}",fontFamily=Book,fontSize=10.sp,color=Red.copy(.75f))
      Text(t.versi,fontFamily=Book,fontSize=12.sp,lineHeight=15.5.sp,color=Ink)
     }
    }
   }
   Column(Modifier.fillMaxWidth().padding(start=DivinaeReaderEngine.cantoOne.safeZone.bodyStart,end=DivinaeReaderEngine.cantoOne.safeZone.bodyEnd),horizontalAlignment=Alignment.CenterHorizontally){
    Text("Tocca una terzina per comprenderla",fontFamily=Book,fontStyle=FontStyle.Italic,fontSize=9.sp,color=Ink.copy(.68f))
   }
   Row(Modifier.fillMaxWidth().padding(start=DivinaeReaderEngine.cantoOne.safeZone.bodyStart,end=DivinaeReaderEngine.cantoOne.safeZone.bodyEnd,top=7.dp),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){
    Text("‹",Modifier.clickable(enabled=page>0){page--;selected=null}.padding(8.dp),fontFamily=Book,fontSize=28.sp,color=if(page>0) Red else Ink.copy(.2f))
    Text("${page+1} / 2",fontFamily=Book,fontSize=12.sp,color=Ink)
    Text("›",Modifier.clickable(enabled=page<1){page++;selected=null}.padding(8.dp),fontFamily=Book,fontSize=28.sp,color=if(page<1) Red else Ink.copy(.2f))
   }
  }
  Box(Modifier.align(Alignment.CenterEnd)){
   if(!tabsVisible && drawer==null){
    Text("‹",Modifier.clip(RoundedCornerShape(topStart=10.dp,bottomStart=10.dp)).background(Color(0xFF4A2417).copy(.82f))
     .clickable{tabsVisible=true}.padding(horizontal=5.dp,vertical=18.dp),fontFamily=Book,fontSize=18.sp,color=Gold)
   }
   AnimatedVisibility(tabsVisible && drawer==null,enter=fadeIn(),exit=fadeOut()){
    Column(verticalArrangement=Arrangement.spacedBy(8.dp),horizontalAlignment=Alignment.End){
     Text("›",Modifier.clickable{tabsVisible=false}.padding(8.dp),fontFamily=Book,fontSize=18.sp,color=Gold)
     listOf("♫" to "Musica","🔥" to "Atmosfera","☰" to "Indice").forEach{(icon,name)->
      Text(icon,Modifier.clip(RoundedCornerShape(topStart=10.dp,bottomStart=10.dp)).background(Color(0xFF4A2417).copy(.92f))
       .clickable{drawer=name}.padding(horizontal=10.dp,vertical=11.dp),fontSize=19.sp,color=Gold)
     }
    }
   }
  }
  AnimatedVisibility(drawer!=null,enter=fadeIn(),exit=fadeOut(),modifier=Modifier.align(Alignment.CenterEnd)){
   Column(Modifier.padding(end=38.dp).width(190.dp).background(Color(0xFFF3E8CF)).clickable{drawer=null;tabsVisible=false}.padding(16.dp)){
    Text(drawer?:"",fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=18.sp,color=Red)
    Spacer(Modifier.height(8.dp))
    when(drawer){
     "Musica"->{
      Text(DivinaeReaderEngine.darkWood.title,fontFamily=Modern,fontSize=13.sp,lineHeight=18.sp,color=Ink)
      Text(if(musicOn)"❚❚  Pausa" else "▶  Riproduci",Modifier.fillMaxWidth().clickable{musicOn=!musicOn}.padding(vertical=10.dp),fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=15.sp,color=Red)
      Slider(value=musicVolume,onValueChange={musicVolume=it})
     }
     "Atmosfera"->{
      Text("Camino",fontFamily=Modern,fontSize=14.sp,color=Ink)
      Text(if(fireOn)"🔥  Spegni" else "🔥  Accendi",Modifier.fillMaxWidth().clickable{fireOn=!fireOn}.padding(vertical=10.dp),fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=15.sp,color=Red)
      Slider(value=fireVolume,onValueChange={fireVolume=it})
     }
     else->Text("INFERNO\n• Canto I — La selva oscura\n\nPURGATORIO\nPARADISO",fontFamily=Modern,fontSize=14.sp,lineHeight=21.sp,color=Ink)
    }
   }
  }
  AnimatedVisibility(selected!=null,enter=fadeIn(),exit=fadeOut(),modifier=Modifier.fillMaxSize()){
   selected?.let{Explanation(it,cantoI[it]){selected=null}}
  }
 }
}

@Composable private fun Explanation(index:Int,t:Terzina,onClose:()->Unit){
 var deep by rememberSaveable(index){mutableStateOf(false)}
 Box(Modifier.fillMaxSize().background(Color(0xFF21130D).copy(.96f)).clickable{onClose()}){
  Column(
   Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()
    .padding(horizontal=20.dp,vertical=24.dp).clip(RoundedCornerShape(14.dp))
    .background(Color(0xFFF3E5C3)).padding(horizontal=22.dp,vertical=20.dp)
    .verticalScroll(rememberScrollState())
  ){
   Text("INFERNO · CANTO I",fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=11.sp,letterSpacing=2.sp,color=Red)
   Text("VERSI ${index*3+1}–${index*3+3}",fontFamily=Modern,fontSize=11.sp,color=Ink.copy(.58f))
   Text(t.versi,fontFamily=Book,fontSize=19.sp,lineHeight=26.sp,color=Ink,modifier=Modifier.padding(top=18.dp))
   Box(Modifier.padding(vertical=17.dp).fillMaxWidth().height(1.dp).background(Gold.copy(.55f)))
   Section("In parole di oggi",t.moderno)
   Section("Che cosa sta dicendo Dante",t.nota)
   Text(if(deep)"Nascondi approfondimento" else "Approfondisci  ›",
    Modifier.padding(top=22.dp).clip(RoundedCornerShape(6.dp)).background(Gold.copy(.16f))
     .clickable{deep=!deep}.padding(horizontal=12.dp,vertical=9.dp),
    fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=15.sp,color=Red)
   AnimatedVisibility(deep){
    Column{
     Section("Contesto",t.contesto)
     Section("Simboli e significato",t.simboli)
     Section("Parole da conoscere",t.lessico)
    }
   }
   Text("Tocca fuori dalla scheda per tornare al manoscritto",fontFamily=Modern,fontStyle=FontStyle.Italic,
    fontSize=11.sp,color=Ink.copy(.5f),textAlign=TextAlign.Center,modifier=Modifier.fillMaxWidth().padding(top=28.dp,bottom=8.dp))
  }
 }
}

@Composable private fun Section(title:String,body:String){
 if(body.isBlank())return
 Text(title,fontFamily=Modern,fontWeight=FontWeight.Bold,fontSize=17.sp,color=Red,modifier=Modifier.padding(top=14.dp))
 Text(body,fontFamily=Modern,fontSize=16.sp,lineHeight=23.sp,color=Ink,modifier=Modifier.padding(top=5.dp))
}
