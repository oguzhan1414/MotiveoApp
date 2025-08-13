// MotivationProvider.kt
package eu.tutorials.motiveoapp.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

object MotivationProvider {
    private val motivationMessages = listOf(
        "Success doesn’t come from what you do occasionally, it comes from what you do consistently.",
        "The future depends on what you do today.",
        "Don’t watch the clock; do what it does. Keep going.",
        "The harder you work for something, the greater you’ll feel when you achieve it.",
        "Push yourself, because no one else is going to do it for you.",
        "Believe you can and you're halfway there.",
        "The only way to do great work is to love what you do.",
        "Dream big and dare to fail.",
        "Success is walking from failure to failure with no loss of enthusiasm.",
        "It always seems impossible until it's done.",
        "Don't stop when you're tired. Stop when you're done.",
        "Wake up with determination. Go to bed with satisfaction.",
        "Do something today that your future self will thank you for.",
        "Little things make big days.",
        "It's going to be hard, but hard does not mean impossible.",
        "The secret of getting ahead is getting started.",
        "The expert in anything was once a beginner.",
        "No pressure, no diamonds.",
        "Tough times never last, but tough people do.",
        "Strive for progress, not perfection.",
        "The only limit to our realization of tomorrow is our doubts of today.",
        "You don't have to be great to start, but you have to start to be great.",
        "Hustle until your haters ask if you're hiring.",
        "Work hard in silence, let success make the noise.",
        "Your only limit is your mind.",
        "Opportunities don't happen. You create them.",
        "Dream without fear. Love without limits.",
        "Be so good they can't ignore you.",
        "Success is the sum of small efforts repeated daily.",
        "The pain you feel today will be the strength you feel tomorrow.",
        "Make each day your masterpiece.",
        "Discipline is choosing between what you want now and what you want most.",
        "Small steps every day lead to big results.",
        "You are capable of amazing things.",
        "The moment you're ready to quit is usually the moment right before the miracle happens.",
        "Don't count the days, make the days count.",
        "You didn't come this far to only come this far.",
        "Rise up, start fresh, see the bright opportunity in each new day.",
        "The only place where success comes before work is in the dictionary.",
        "You don't get what you wish for, you get what you work for.",
        "Every champion was once a contender that refused to give up.",
        "The difference between ordinary and extraordinary is that little extra.",
        "Don't wait for opportunity. Create it.",
        "Success is not the key to happiness. Happiness is the key to success.",
        "The best way to predict the future is to create it.",
        "Your attitude determines your direction.",
        "Great things never come from comfort zones.",
        "The only person you should try to be better than is the person you were yesterday.",
        "Limitations live only in our minds. But if we use our imaginations, our possibilities become limitless.",
        "The road to success and the road to failure are almost exactly the same.",
        "What you do today can improve all your tomorrows.",
        "The only thing standing between you and your goal is the story you keep telling yourself.",
        "You are never too old to set another goal or to dream a new dream.",
        "The man who moves a mountain begins by carrying away small stones.",
        "Do not stop thinking of life as an adventure. You have no security unless you can live bravely.",
        "The best revenge is massive success.",
        "Act as if what you do makes a difference. It does.",
        "The only thing that overcomes hard luck is hard work.",
        "Energy and persistence conquer all things.",
        "The difference between a stumbling block and a stepping stone is how high you raise your foot.",
        "The secret to getting ahead is getting started."
    )

    fun getTodayMotivation(): String {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val index = today.hashCode().absoluteValue % motivationMessages.size
        return motivationMessages[index]
    }
}