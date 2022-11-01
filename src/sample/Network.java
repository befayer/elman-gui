package sample;

import java.util.ArrayList;
import java.util.Arrays;

public class Network {

    /*private int epochs = 100;//количество эпох
    private final double alpha = 0.5;//коэффициент обучения от 0 до 1
    private final double K = 0.4;//спрошу рандомно или нет задается на каждой итерации
    private final double[] enters; //входной массив нейронов
    private final double[] hidden; //массив нейронов скрытого слоя
    private final double[] output; //выходной массивф
    private final double[] gS;//сумма произведений весов на значения нейронов скрытого слоя
    private final double[] uI;//сумма произведений значений входных нейронов на веса
    private double[][] wEH; // веса входного слоя
    private final double[][] wEHPrevious; // веса входного слоя на прошлой итерации обучения
    private double[][] wHO; // веса скрытого слоя
    private final double[][] wHOPrevious;// веса выходного слоя на прошлой итерации обучения
    private final double[][] trainData;*/

    private int epochs = 5;//количество эпох
    private final double alpha;//коэффициент обучения от 0 до 1  = 0.3
    private final double K;//спрошу рандомно или нет задается на каждой итерации  = 0.05
    private final double[] enters; //входной массив нейронов
    private final double[] hidden; //массив нейронов скрытого слоя
    private final double[] output; //выходной массивф
    private final double[] gS;//сумма произведений весов на значения нейронов скрытого слоя
    private final double[] uI;//сумма произведений значений входных нейронов на веса
    private double[][] wEH; // веса входного слоя
    private final double[][] wEHPrevious; // веса входного слоя на прошлой итерации обучения
    private double[][] wHO; // веса скрытого слоя
    private final double[][] wHOPrevious;// веса выходного слоя на прошлой итерации обучения
    private final double[][] trainData;

    ArrayList<String> arrayList;

    public Network(int countEpochs, double alpha, double K, int countFeatures, int countClasses, double[][] trainData) {
        epochs = countEpochs;
        this.alpha = alpha;
        this.K = K;
        int hiddenLimit = countFeatures + countClasses / 2;
        this.trainData = trainData;
        enters = new double[countFeatures + hiddenLimit + 1];
        enters[0] = 1;
        hidden = new double[hiddenLimit + 1];
        hidden[0] = 1;
        output = new double[countClasses];
        uI = new double[hiddenLimit + 1];
        gS = new double[countClasses];
        uI[0] = 1;
        wEH = new double[enters.length][hidden.length];
        wHO = new double[hidden.length][output.length];
        wEHPrevious = new double[enters.length][hidden.length];
        wHOPrevious = new double[hidden.length][output.length];
        wEH = initWeight(wEH, -0.5, 0.5);
        wHO = initWeight(wHO, -0.5, 0.5);
        arrayList = new ArrayList<>();
    }

    /**
     * Прямое распространение.
     * 2. Для очередного момента определить состояние всех нейронов
     * сети (сигналы vl и yl).
     * На этой основе можно сформировать входной вектор для произвольного момента t.
     */

    //прямое распространение
    public double[] directDistribution(double[] testData) {
        System.arraycopy(testData, 0, enters, 1, enters.length - hidden.length);// инициалиазция входных нейронов (4)
        initContext();
        for (int i = 1; i < hidden.length; i++) {
            uI[i] = 0;
            for (int j = 0; j < enters.length; j++) {
                uI[i] += enters[j] * wEH[j][i];
            }
            hidden[i] = functionOfActivation(uI[i]);
        }
        for (int s = 0; s < output.length; s++) {
            gS[s] = 0;
            for (int i = 0; i < hidden.length; i++) {
                gS[s] += hidden[i] * wHO[i][s];
            }
            output[s] = functionOfActivation(gS[s]);
        }
        return output;
    }

    /**
     * Алгоритм наискорейшего спуска
     */
    public void elman() {
        double err;
        while (--epochs >= 0) {
            err = 0;
            Arrays.fill(enters, 0);
            enters[0] = 1;
            //После уточнения значений весов перейти к пункту 2 алгоритма для расчета в очередной момент времени.
            for (int t = 0; t < trainData.length; t++) {

                directDistribution(trainData[t]);//матрица признаков

                //3. Определить вектор погрешности обучения для нейронов
                //выходного слоя как разность между фактическим и ожидаемым
                //значениями сигналов выходных нейронов.
                //126
                double[] e = objectiveFunction(output, trainData, t, err);

                //4. Сформировать вектор градиента целевой функции относительно
                //весов выходного и скрытого слоя с использованием формул (129), (133) и (134).
                //129 считаем градиент для выходного слоя
                double[][] gradientOut = new double[hidden.length][output.length];// какая размерность вектора градиента? (K+1)*M
                for (int i = 0; i < hidden.length; i++) {
                    for (int s = 0; s < output.length; s++) {
                        gradientOut[i][s] += e[s] * sigmaActivation(gS[s]) * hidden[i];
                    }
                }

                //134 производные целевой функции относительно весов скрытого слоя
                double[] dv = new double[hidden.length];
                for (int i = 1; i < hidden.length; i++) {
                    for (int k = 1; k < hidden.length; k++) {
                        dv[i] += sigmaActivation(enters[k + (enters.length - hidden.length)])
                                * wEH[k + (enters.length - hidden.length)][i];
                    }
                }
                for (int i = (enters.length - hidden.length + 1); i < enters.length; i++) {
                    dv[i - (enters.length - hidden.length)] =
                            sigmaActivation(uI[i - (enters.length - hidden.length)])
                                    * (enters[i] + dv[i - (enters.length - hidden.length)]);
                }

                //132
                //Считаем компоненты вектора градиента целевой функции относительно весов смкрытого слоя
                double[] part = new double[hidden.length];
                for (int i = 1; i < hidden.length; i++) {
                    for (int s = 0; s < output.length; s++) {
                        part[i] += dv[i] * wHO[i][s];
                    }
                }

                //Считаем градиент 133
                double[][] gradientFromWeightOfHidden = new double[enters.length][hidden.length];
                for (int j = 0; j < enters.length; j++) {
                    for (int i = 1; i < hidden.length; i++) {
                        for (int s = 0; s < output.length; s++) {
                            gradientFromWeightOfHidden[j][i] += e[s] * sigmaActivation(gS[s]) * part[i];
                        }
                    }
                }


                //5. Уточнить значения весов сети согласно правилам метода наискорейшего спуска:
                // для нейронов выходного слоя сети по формуле (136) + (138)
                //веса между скрытым и входным
                double clarification;
                for (int i = 0; i < hidden.length; i++) {
                    for (int s = 0; s < output.length; s++) {
                        clarification = (-1 * alpha * gradientOut[i][s]) + K * wHOPrevious[i][s];
                        wHO[i][s] = wHO[i][s] + clarification;
                        wHOPrevious[i][s] = clarification;
                    }
                }
                //5. Уточнить значения весов сети согласно правилам метода наискорейшего спуска:
                // для нейронов скрытого слоя сети по формуле (137) + (138)
                //между входным вектором и скрытым
                for (int j = 0; j < enters.length; j++) {
                    for (int i = 1; i < hidden.length; i++) {
                        clarification = (-1 * alpha * gradientFromWeightOfHidden[j][i]) + K * wEHPrevious[j][i];
                        wEH[j][i] = wEH[j][i] + clarification;//коэф рандомим?
                        wEHPrevious[j][i] = clarification;
                    }
                }
            }
        }
    }

    //целевая функция
    public double[] objectiveFunction(double[] output, double[][] trainData, int t, double err){
        double[] e = new double[output.length];
        for (int j = 0; j < output.length; j++) {
            if (j + 1 == trainData[t][trainData[0].length - 1])
                e[j] = output[j] - 1;
            else {
                e[j] = output[j];
            }
        }
        //целевая функция
        double eOfOut = 0;
        for (int j = 0; j < output.length; j++) {
            eOfOut += Math.pow(e[j], 2);
        }
        err += eOfOut;
        if (t == trainData.length - 1) {
            System.out.println("Error: " + err * 0.5);
            arrayList.add("Error: " + err * 0.5);
        }
        return e;
    }

    public ArrayList<String> getArrayList(){
        return this.arrayList;
    }

    public void initContext() {
        for (int i = 1; i < hidden.length; i++) {
            enters[enters.length - hidden.length + i] = hidden[i];
        }
    }

    //Логистическая функция активации
    private double functionOfActivation(double x) {
        return (1 / (1 + Math.pow(Math.E, (-1 * x))));
    }

    //производная от сигмоидальной функции
    private double sigmaActivation(double x) {
        double f = (1 / (1 + Math.pow(Math.E, (-1 * x))));
        return f * (1 - f);
    }

    public void setEntersZero() {
        Arrays.fill(enters, 0);
        enters[0] = 1;
    }

    public double[][] initWeight(double[][] weights, double min, double max) {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = (Math.random() * (max - min) + min);
            }
        }
        return weights;
    }
}
