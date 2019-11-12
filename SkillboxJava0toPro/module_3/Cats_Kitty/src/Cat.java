public class Cat
{
    private Double originWeight;
    private Double originKittenWeight;
    private Double randomWeight;
    private Double feedMass = 0.0D;
    private Double shit;
    private Boolean kitten;
    private Boolean dead;
    private Double assignedWeight;
    private Double minWeight;
    private Double maxWeight;
    private Double minKittenWeight;
    private Double maxKittenWeight;
    public static int count = 0;
    public static int liveCount = 0;
    public static int deathCount = 0;
    public String name;

    public String getName() {
        return this.name;
    }

    public Cat(Double assignedWeight, String name)
    {
        this.assignedWeight = assignedWeight;
        this.name = name;
        this.dead = false;
        if (assignedWeight >= 1000.0D & assignedWeight <= 9000.0D) {
            this.randomWeight = assignedWeight;
            this.originWeight = this.randomWeight;
            ++liveCount;
            ++count;
            this.minWeight = 1000.0D;
            this.maxWeight = 9000.0D;
            this.shit = 200.0D;
            this.kitten = false;
        } else if (assignedWeight >= 100.0D & assignedWeight <= 200.0D) {
            this.originKittenWeight = assignedWeight;
            this.minKittenWeight = 100.0D;
            this.maxKittenWeight = 200.0D;
            this.shit = 20.0D;
            this.kitten = true;
            ++liveCount;
            ++count;
        } else {
            ++deathCount;
            this.dead = true;
            if (assignedWeight < 300.0D) {
                this.kitten = true;
                this.originKittenWeight = assignedWeight;
                this.minKittenWeight = 100.0D;
                this.maxKittenWeight = 200.0D;
            } else {
                this.kitten = false;
                this.randomWeight = assignedWeight;
                this.originWeight = this.randomWeight;
                this.minWeight = 1000.0D;
                this.maxWeight = 9000.0D;
            }
        }
    }

    public Cat(String name)
    {
        this.name = name;
        this.dead = false;
        if (Math.random() > 0.35D)
        {
            this.kitten = false;
        }
        else
            {
                this.kitten = true;
            }

        if(!kitten)
        {
            this.randomWeight = 1500.0D + 3000.0D * Math.random();
            this.originWeight = this.randomWeight;
            this.minWeight = 1000.0D;
            this.maxWeight = 9000.0D;
            this.shit = 200.0D;
            ++liveCount;
            ++count;
        }
        else
            {
                this.assignedWeight = 100.0D + 50.0D * Math.random();
                this.originKittenWeight = this.assignedWeight;
                this.minKittenWeight = 100.0D;
                this.maxKittenWeight = 200.0D;
                this.shit = 10.0D;
                ++liveCount;
                ++count;
                this.kitten = true;
            }
    }

    public Cat()
    {
        if (Math.random() > 0.35D)
        {
            this.randomWeight = 1500.0D + 3000.0D * Math.random();
            this.originWeight = this.randomWeight;
            this.minWeight = 1000.0D;
            this.maxWeight = 9000.0D;
            this.shit = 200.0D;
            ++liveCount;
            ++count;
            this.kitten = false;
            this.dead = false;
        }
        else
            {
            this.assignedWeight = 100.0D + 50.0D * Math.random();
            this.originKittenWeight = this.assignedWeight;
            this.minKittenWeight = 100.0D;
            this.maxKittenWeight = 200.0D;
            this.shit = 10.0D;
            ++liveCount;
            ++count;
            this.kitten = true;
            this.dead = false;
            }
    }

    public void createСlone(String name, Double weight, Boolean kitten)
    {
        this.name = name;
        this.kitten = kitten;
        if (!kitten) {
            this.randomWeight = weight;
            this.originWeight = weight;
            this.minWeight = 1000.0D;
            this.maxWeight = 9000.0D;
            this.shit = 200.0D;
            ++liveCount;
            ++count;
            this.dead = false;
        }
        else
            {
            this.assignedWeight = weight;
            this.originKittenWeight = weight;
            this.minKittenWeight = 100.0D;
            this.maxKittenWeight = 200.0D;
            this.shit = 10.0D;
            ++liveCount;
            ++count;
            this.dead = false;
            }
    }

    public void meow() {
        if (!this.dead) {
            if (!this.kitten) {
                this.randomWeight = this.randomWeight - 500.0D;
                if (this.randomWeight < this.minWeight) {
                    this.dead = true;
                    --liveCount;
                    ++deathCount;
                    System.out.println("Meeeeeow!");
                } else {
                    System.out.println("Meow!");
                }
            } else {
                this.assignedWeight = this.assignedWeight - 20.0D;
                if (this.assignedWeight < this.minKittenWeight) {
                    this.dead = true;
                    --liveCount;
                    ++deathCount;
                    System.out.println("muuuurr!");
                } else {
                    System.out.println("murr!");
                }
            }
        } else {
            System.out.println("Dead cats don't meow.");
        }

    }

    public void shits() {
        if (!this.dead) {
            if (!this.kitten) {
                this.shit = 150.0D + 300.0D * Math.random();
                this.randomWeight = this.randomWeight - this.shit;
                if (this.randomWeight < this.minWeight) {
                    this.dead = true;
                    --liveCount;
                    ++deathCount;
                } else {
                    this.dead = false;
                }
            } else {
                this.shit = 5.0D + 20.0D * Math.random();
                this.assignedWeight = this.assignedWeight - this.shit;
                if (this.assignedWeight < this.minKittenWeight) {
                    this.dead = true;
                    --liveCount;
                    ++deathCount;
                } else {
                    this.dead = false;
                }
            }
        } else {
            System.out.println(this.name + " can't shit, between he is Dead.");
        }

    }

    public void feed(Double amount) {
        if (!this.dead) {
            if (!this.kitten) {
                this.randomWeight = this.randomWeight + amount;
                this.feedMass = this.feedMass + amount;
                System.out.println(this.name + " feed " + amount);
                if (this.randomWeight > this.maxWeight) {
                    this.dead = true;
                    --liveCount;
                    ++deathCount;
                } else {
                    this.dead = false;
                }
            } else {
                this.assignedWeight = this.assignedWeight + amount;
                this.feedMass = this.feedMass + amount;
                System.out.println(this.name + " feed " + amount);
                if (this.assignedWeight > this.maxKittenWeight) {
                    this.dead = true;
                    --liveCount;
                    ++deathCount;
                } else {
                    this.dead = false;
                }
            }
        } else {
            System.out.println(this.name + " can't feed, between he is Dead.");
        }

    }

    public void drink(Double amount) {
        if (!this.kitten) {
            this.randomWeight = this.randomWeight + amount;
        } else {
            this.assignedWeight = this.assignedWeight + amount;
        }

    }

    public Double getFeedMass() {
        return this.feedMass;
    }

    public Double getWeight() {
        return !this.kitten ? this.randomWeight : this.assignedWeight;
    }

    public Double getShit() {
        return this.shit;
    }

    public String getStatus() {
        if (!this.kitten) {
            if (this.randomWeight < this.minWeight) {
                this.dead = true;
                return "Dead.";
            } else if (this.randomWeight > this.maxWeight) {
                this.dead = true;
                return "Exploded.";
            } else if (this.originWeight < this.randomWeight) {
                this.dead = false;
                return "Sleeping.";
            } else {
                this.dead = false;
                return "Playing.";
            }
        } else if (this.assignedWeight < this.minKittenWeight) {
            this.dead = true;
            return "Dead.";
        } else if (this.assignedWeight > this.maxKittenWeight) {
            this.dead = true;
            return "Exploded.";
        } else if (this.originKittenWeight < this.assignedWeight) {
            this.dead = false;
            return "Sleeping.";
        } else {
            this.dead = false;
            return "Playing.";
        }
    }

    public static int getLiveCount() {
        if (liveCount < 0) {
            liveCount = 0;
            return liveCount;
        } else {
            return liveCount;
        }
    }

    public static int getDeathCount() {
        return deathCount;
    }

    public Boolean getKitten() {
        return this.kitten ? true : false;
    }

    public String isKitten() {
        return this.kitten ? "kitten" : "cat";
    }

    public Boolean getDead() {
        return this.dead ? true : false;
    }

    public String isDead() {
        return this.dead ? "dead" : "live";
    }

    public static int getCount() {
        return count;
    }
}
