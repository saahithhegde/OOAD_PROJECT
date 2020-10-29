import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LandingHomepageComponent } from './landing-homepage.component';

describe('LandingHomepageComponent', () => {
  let component: LandingHomepageComponent;
  let fixture: ComponentFixture<LandingHomepageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LandingHomepageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LandingHomepageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});


