package agh.cs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genotype {

    private int amountOfGenes=32;
    private int amountOfTypes=8;
    private final List<Integer> genes= new ArrayList<>();

    /* konstruktor tylko dla generowanych zwierząt na początku, randomowe geny */

    public Genotype(){
        Random random=new Random();
        for(int i=0;i<this.amountOfGenes-this.amountOfTypes;i++){
            this.genes.add(Math.abs((random.nextInt()) % 8));
        }
        for(int i=0;i<this.amountOfTypes;i++){
            this.genes.add(i);
        }
        this.genes.sort(Integer::compareTo);
    }

    /* konstruktor, który wyznacza geny bazując na genotypach rodziców */

    public Genotype(Genotype parent1,Genotype parent2){
        Random random=new Random();
        int c1= Math.abs(random.nextInt()%(this.amountOfGenes-2));
        int c2= 1+c1+Math.abs(random.nextInt()%(this.amountOfGenes-2-c1));
        this.genes.addAll(parent1.genes.subList(0,c1+1));
        this.genes.addAll(parent2.genes.subList(c1+1,c2+1));
        this.genes.addAll(parent1.genes.subList(c2+1,this.amountOfGenes));
        int[] count=this.countTypes();
        for(int i=0;i<this.amountOfTypes;i++){
            if (count[i]==0){
                for(int j=0;j<this.amountOfTypes;j++){
                    if(count[j]>1){
                        this.genes.remove(j);
                        this.genes.add(i);
                        break;
                    }
                }
            }
        }
        this.genes.sort(Integer::compareTo);
    }

    /* zliczanie genów jednego typu */

    public int[] countTypes(){
        int count[]=new int[8];
        for(Integer gene: this.genes){
            count[gene]+=1;
        }
        return count;
    }

    /* zwrot tablicy genów w formie tekstu */

    public String toString(){
        return this.genes.toString();
    }

    /* getter na liczbę genów */

    public int getAmountOfGenes(){
        return this.amountOfGenes;
    }

    /* zwraca n-ty w kolejności gen */

    public int getNthGene(int n){
        return this.genes.get(n);
    }

    /* zwraca gen dominujący, tzn najczęściej występujący
    w przypadku remisu, umownie postanowiłem zwracać wtedy gen wcześniejszy, ponieważ uznałem że to już jest kwestia
    umowna, następnie konsekwentnie przyjmowałem takie rozstrzygnięcie wszędzie gdzie potrzebny był dominujący gen
     */

    public int getDominant(){
        int[] counter=this.countTypes();
        int maxGene=0;
        int maxGeneCount=counter[0];
        for(int i=1;i<8;i++){
            if(maxGeneCount<counter[i]){
                maxGene=i;
                maxGeneCount=counter[i];
            }
        }
        return maxGene;
    }

}
