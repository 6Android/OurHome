package com.ssafy.ourhome.screens.question.pet


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ssafy.domain.model.pet.DomainFamilyPetDTO
import com.ssafy.ourhome.components.MainAppBar
import com.ssafy.ourhome.components.OurHomeSurface
import com.ssafy.ourhome.components.pie.PieChart
import com.ssafy.ourhome.components.pie.PieChartData
import com.ssafy.ourhome.components.pie.PieChartData.Slice
import com.ssafy.ourhome.components.pie.animation.simpleChartAnimation
import com.ssafy.ourhome.components.pie.renderer.SimpleSliceDrawer
import com.ssafy.ourhome.screens.question.CenterHorizontalColumn
import com.ssafy.ourhome.screens.question.QuestionViewModel
import com.ssafy.ourhome.ui.theme.Gray
import com.ssafy.ourhome.ui.theme.MainColor
import com.ssafy.ourhome.ui.theme.PieChartColors
import com.ssafy.ourhome.utils.State


@Composable
fun PetDetailScreen(navController: NavController, vm: QuestionViewModel) {
    val scrollState = rememberScrollState()
    val familyContributeList = remember {
        mutableStateOf(PieChartData(listOf()))
    }
//        PieChartData(listOf(Slice(0.5F, Color.Red, "아빠"), Slice(0.3F, Color.Blue, "엄마"), Slice(0.2F, Color.Green, "아들")))
    val context = LocalContext.current

    initPetDetailScreen(vm)
    initPetDetailViewModelCallback(vm, context, familyContributeList)

    Scaffold(topBar = {
        MainAppBar(title = "펫 상세", onBackClick = {
            navController.popBackStack()
    })}) {
        OurHomeSurface() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                CenterHorizontalColumn{

                    PetDetail(vm.pet)

                }

                Spacer(modifier = Modifier.height(32.dp))

                PetExp(vm.pet)

                Spacer(modifier = Modifier.height(24.dp))

                CenterHorizontalColumn {

                    FamilyExpPieChart(familyContributeList)

                }
                
                Spacer(modifier = Modifier.height(36.dp))
                
                FamilyExpLazyRow(familyContributeList)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }

}

fun initPetDetailScreen(vm: QuestionViewModel){
    vm.getFamilyUsersInPetDetail()
}

fun initPetDetailViewModelCallback(vm: QuestionViewModel, context: Context, familyContributeList: MutableState<PieChartData>){

    when (vm.familyPetProcessState) {
        State.ERROR -> {
            Toast.makeText(context, "펫 정보를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show()
            vm.familyPetProcessState = State.DEFAULT
        }
        State.SUCCESS -> {
            var totalContributePoint = 0L
            for(key in vm.familyUsers.value.keys){
                totalContributePoint += vm.familyUsers.value.get(key)!!.contribute_point
            }

            val dataList = mutableListOf<Slice>()
            var i = 0
            for(key in vm.familyUsers.value.keys){
                dataList.add(Slice(1F * vm.familyUsers.value.get(key)!!.contribute_point / totalContributePoint, PieChartColors[i], vm.familyUsers.value.get(key)!!.name))
                i++
            }
            familyContributeList.value = PieChartData(dataList)
        }
    }
}

/** 가족 경험치 기여도 정보 리스트 **/
@Composable
fun FamilyExpLazyRow(familyContributeList: MutableState<PieChartData>){
    LazyRow{
        items(familyContributeList.value.slices.size){
            FamilyExpLazyRowItem(familyContributeList.value.slices.get(it))
        }
    }
}

/** 가족 경험치 기여도 lazyRow item **/
@Composable
fun FamilyExpLazyRowItem(familyContributeList: Slice){
    Row {
        Box(modifier = Modifier
            .width(36.dp)
            .height(8.dp)
            .padding(end = 8.dp)
            .background(familyContributeList.color))

        Text(text = familyContributeList.name,
            style = MaterialTheme.typography.body2

        )
        
        Spacer(modifier = Modifier.width(8.dp))

    }
}

/** 가족 경험치 기여도 piechart **/
@Composable
fun FamilyExpPieChart(familyContributeList: MutableState<PieChartData>){
    PieChart(
        pieChartData = familyContributeList.value,
    modifier = Modifier
        .width(300.dp)
        .height(300.dp),
    animation = simpleChartAnimation(),
    sliceDrawer = SimpleSliceDrawer()
    )

}

/** 펫 경험치 text and progressbar **/
@Composable
fun PetExp(pet: DomainFamilyPetDTO) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "경험치",
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.ExtraBold)
        )

        Spacer(modifier = Modifier.width(12.dp))

        customProgressBar(progress = (pet.pet_level * 100 / pet.next_level_exp))
    }
}

/** 경험치 프로그래스 바 **/
@Composable
fun customProgressBar(progress: Int) {
    // in this method we are creating a column
    Column(
        // in this column we are specifying modifier to
        // align the content within the column
        // to center of the screen.
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
            .fillMaxHeight(),

        // on below line we are specifying horizontal
        // and vertical alignment for the content of our column
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // in this column we are creating a variable
        // for the progress of our progress bar.

        // on the below line we are creating a box.
        Box(
            // inside this box we are adding a modifier
            // to add rounded clip for our box with
            // rounded radius at 15
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                // on below line we are specifying
                // height for the box
                .height(30.dp)

                // on below line we are specifying
                // background color for box.
                .background(Gray)

                // on below line we are
                // specifying width for the box.
                .width(300.dp)
        ) {
            // in this box we are creating one more box.
            Box(
                // on below line we are adding modifier to this box.
                modifier = Modifier
                    // on below line we are adding clip \
                    // for the modifier with round radius as 15 dp.
                    .clip(RoundedCornerShape(15.dp))

                    // on below line we are
                    // specifying height as 30 dp
                    .height(30.dp)

                    // on below line we are adding background
                    // color for our box as brush
                    .background(
                        // on below line we are adding brush for background color.
                        Brush.horizontalGradient(
                            // in this color we are specifying a gradient
                            // with the list of the colors.
                            listOf(
                                // on below line we are adding two colors.
                                MainColor,
                                Color(0xFFFF622B)
                            )
                        )
                    )
                    // on below line we are specifying width for the inner box
                    .width(300.dp * progress / 100)
            )
            // on below line we are creating a text for our box
            Text(
                // in text we are displaying a text as progress bar value.
                text = "$progress %",

                // on below line we are adding
                // a modifier to it as center.
                modifier = Modifier.align(Alignment.Center),

                // on below line we are adding
                // font size to it.
                fontSize = 15.sp,

                // on below line we are adding
                // font weight as bold.
                fontWeight = FontWeight.Bold,

                // on below line we are
                // specifying color for our text
                color = Color.White
            )
        }

    }
}


/** 펫 정보 (이름, 이미지, 레벨, 설명, 퀘스트) **/
@Composable
fun PetDetail(pet: DomainFamilyPetDTO) {
    Text(
        text = pet.name,
        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
    )

    Spacer(modifier = Modifier.height(4.dp))

    Image(
        modifier = Modifier.size(250.dp),
        painter = rememberAsyncImagePainter(pet.image),
        contentDescription = "펫 이미지"
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = "Lv. " + pet.pet_level.toString(),
        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)

    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = pet.description.replace("\\n", "\n"),
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "질문에 대한 답변을 많이 하고 \n 답변을 길게 작성하면 빠른 성장을 할 수 있어요.",
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center
    )
}