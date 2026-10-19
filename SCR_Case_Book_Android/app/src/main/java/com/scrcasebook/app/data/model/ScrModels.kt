package com.scrcasebook.app.data.model

data class ScrSection(
    val index: Int,
    val key: String,
    val title: String,
    val hint: String,
    val hasChiefComplaintColumns: Boolean = false
)

enum class PalmRejectionMode(val label: String) {
    PEN_ONLY("✏️ Pen only"),
    PEN_AND_FINGER("✏️ + ☝️ Pen & Finger"),
    SCROLL("✋ Scroll")
}

data class Stroke(
    val c: String = "#111111",      // Color hex
    val w: Float = 2.5f,             // Width in dp / pixels
    val e: Int = 0,                  // 1 if eraser, 0 if pen
    val p: List<Float> = emptyList() // Flattened coordinates [x0, y0, x1, y1, ...]
)

object ScrDefinitions {
    val PALETTE = listOf("#111111", "#1747C9", "#C01818")

    val SECTIONS = listOf(
        ScrSection(
            index = 0,
            key = "s0",
            title = "Case particulars",
            hint = "S. No · Physician · Observer · OPD / IPD Reg. No. · Date · Diagnosis · Age · Sex · Education · Occupation · Status · Religion · Veg / N.V. · Spouse · Father · Mother · Siblings · Children · Address · Tel"
        ),
        ScrSection(
            index = 1,
            key = "s1",
            title = "Case summary",
            hint = "Brief clinical summary of the patient case"
        ),
        ScrSection(
            index = 2,
            key = "s2",
            title = "A.I Chief complaint(s)",
            hint = "No. · Location (area, direction, spread, tissue, organ, system & duration) · Sensation & pathology · Modalities (A.F., <, >) · Accompaniments (strict time relation)",
            hasChiefComplaintColumns = true
        ),
        ScrSection(
            index = 3,
            key = "s3",
            title = "A.II Associated complaints (concomitants)",
            hint = "Concomitant symptoms appearing alongside chief complaints"
        ),
        ScrSection(
            index = 4,
            key = "s4",
            title = "III.A.1 Physical characteristics",
            hint = "Appearance · build · weight · face · hands · gait · deformities · skin · hair · nails · eyes · ears · mouth · teeth · coldness / heat · oedema · perspiration · general impression & type"
        ),
        ScrSection(
            index = 5,
            key = "s5",
            title = "III.A.2 Digestion",
            hint = "Acidity · appetite · colics · eructations · flatulence · hunger · nausea · taste · thirst · vomiting · aversions (A) & cravings (C)"
        ),
        ScrSection(
            index = 6,
            key = "s6",
            title = "III.A.3 Eliminations",
            hint = "Stool · urine · abnormal discharges"
        ),
        ScrSection(
            index = 7,
            key = "s7",
            title = "III.A.4 Menstrual function",
            hint = "FMP · LMP · menarche · menses · flow · leucorrhoea · concomitants"
        ),
        ScrSection(
            index = 8,
            key = "s8",
            title = "III.A.5 Sexual function",
            hint = "Desire · masturbation · coitus · erection · ejaculation · orgasm · vagina"
        ),
        ScrSection(
            index = 9,
            key = "s9",
            title = "III.A.6 Patient’s / mother’s obstetric history",
            hint = "Pregnancies · antenatal · labour · delivery · post-partum · particulars of each pregnancy"
        ),
        ScrSection(
            index = 10,
            key = "s10",
            title = "III.A.7 Developmental landmarks & problems",
            hint = "Milestones · speech · control · feeding · behaviour · parental attitudes"
        ),
        ScrSection(
            index = 11,
            key = "s11",
            title = "III.A.8 Diet & daily routine",
            hint = "Meal timings, hydration, sleep-wake cycles, physical routine"
        ),
        ScrSection(
            index = 12,
            key = "s12",
            title = "III.B Mental state",
            hint = "Emotional · reactions · intellectual · activity / inactivity · imbalance · evidence · state: nature & cause"
        ),
        ScrSection(
            index = 13,
            key = "s13",
            title = "Life-space investigation",
            hint = "Society · family · inter-personal · dissatisfaction"
        ),
        ScrSection(
            index = 14,
            key = "s14",
            title = "III.C Reactions – physical factors",
            hint = "Time · temperature · motion & position · meteorological · sensory inputs · physiological functions"
        ),
        ScrSection(
            index = 15,
            key = "s15",
            title = "Dreams: analysis",
            hint = "Recurring themes, symbolism, emotional residue upon waking"
        ),
        ScrSection(
            index = 16,
            key = "s16",
            title = "III.D Fever totality",
            hint = "Onset · type · course · chill · heat · perspiration · compound fevers · apyrexia"
        ),
        ScrSection(
            index = 17,
            key = "s17",
            title = "III.E Original unmodified picture",
            hint = "Natural course of illness before suppressive medications or therapies"
        ),
        ScrSection(
            index = 18,
            key = "s18",
            title = "III.F Chronological sequence",
            hint = "Timeline of ailments from birth to present presentation"
        ),
        ScrSection(
            index = 19,
            key = "s19",
            title = "Past history (personal / family) & check-list",
            hint = "P/H · F/H · addictions · autoimmune · mental disorders · skin"
        ),
        ScrSection(
            index = 20,
            key = "s20",
            title = "Provisional clinical & differential diagnosis",
            hint = "Clinical investigations, differential diagnosis, diagnostic rationale"
        ),
        ScrSection(
            index = 21,
            key = "s21",
            title = "Investigations",
            hint = "Investigation · report · dates"
        ),
        ScrSection(
            index = 22,
            key = "s22",
            title = "Miasmatic expression",
            hint = "Past · personal · family history · predispositions"
        ),
        ScrSection(
            index = 23,
            key = "s23",
            title = "Repertorial technique & totality",
            hint = "Rubrics selected, repertorization chart, grading & analysis"
        ),
        ScrSection(
            index = 24,
            key = "s24",
            title = "Remedy-correspondences & selection",
            hint = "Constitutional · acute · intercurrent totalities (state grounds of final selection)"
        ),
        ScrSection(
            index = 25,
            key = "s25",
            title = "Planning & programming of Rx",
            hint = "Susceptibility · sensitivity · suppressions · correspondence · functional / structural changes · variations · vitality · miasm"
        )
    )
}
